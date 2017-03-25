package com.wjs.schedule.service.server.impl;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.bean.ClientTaskInfoBean;
import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.component.cache.JobClientSessionCache;
import com.wjs.schedule.dao.exec.CuckooClientJobDetailMapper;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.domain.exec.CuckooClientJobDetailCriteria;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.enums.CuckooClientJobStatus;
import com.wjs.schedule.enums.CuckooMessageType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobCanNotRunningException;
import com.wjs.schedule.exception.JobRunningErrorException;
import com.wjs.schedule.net.server.ServerUtil;
import com.wjs.schedule.net.vo.IoClientInfo;
import com.wjs.schedule.service.job.CuckooJobService;
import com.wjs.schedule.service.server.CuckooServerService;
import com.wjs.schedule.vo.job.CuckooClientJobExecResult;

@Service("cuckooServerService")
public class CuckooServerServiceImpl implements CuckooServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooServerServiceImpl.class);
	
	@Autowired
	CuckooClientJobDetailMapper cuckooClientJobDetailMapper;
	

	@Autowired
	CuckooJobService cuckooJobService;
	
	@Override
	public CuckooClientJobExecResult execRemoteJob(List<CuckooClientJobDetail> remoteJobExecs, JobInfoBean jobBean) throws JobCanNotRunningException, JobRunningErrorException {
		
		CuckooClientJobExecResult result = new CuckooClientJobExecResult();
		if(CollectionUtils.isEmpty(remoteJobExecs)){
//			return result;
			throw new JobCanNotRunningException("remoteJobExecs is null");
		}
		// 根据remoteJobExec 获取socket,
		Object socket = null;
		CuckooClientJobDetail socketClient = null;
		for (CuckooClientJobDetail cuckooClientJobDetail : remoteJobExecs) {
			socket = JobClientSessionCache.get(cuckooClientJobDetail.getId());
			if(null != socket){
				
				socketClient = cuckooClientJobDetail;
				break;
			}
		}
		
		result.setClientJobInfo(socketClient);
		result.setClientJobInfo(socketClient);
		// 意外情况获取不到socket
		if(socket == null){
			throw new JobCanNotRunningException("JobClientSessionCache can not get socket");
		}
		
		// 更新远程服务器最新调用时间
		try {
			socketClient.setModifyDate(System.currentTimeMillis());
			cuckooClientJobDetailMapper.updateByPrimaryKeySelective(socketClient);
			// socket写数据,触发客户端任务调度
			LOGGER.info("调用远程任务开始,jobApp:{},jobName:{},bean:{}" , socketClient.getJobClassApplication() ,socketClient.getJobName(), jobBean);
			ServerUtil.send(socketClient, CuckooMessageType.JOBDOING,  jobBean);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {

			LOGGER.error("job exec error:{}",e.getMessage() ,e);
			throw new JobRunningErrorException("job exec error:{}",e.getMessage());
		}
		
	}

	@Override
	public List<CuckooClientJobDetail> getExecRemotesId(Long jobId) {
		
		// 查看任务详细信息
		CuckooJobDetail jobInfo = cuckooJobService.getJobById(jobId);
		
		// 查询可执行服务器详细信息，并返回
		CuckooClientJobDetailCriteria clientCrt = new CuckooClientJobDetailCriteria();
		clientCrt.createCriteria().andJobClassApplicationEqualTo(jobInfo.getJobClassApplication())
		.andJobNameEqualTo(jobInfo.getJobName());
		List<CuckooClientJobDetail> result = cuckooClientJobDetailMapper.selectByExample(clientCrt);

		return result;
	}

	@Override
	@Transactional
	public Long addRemote(IoSession session, ClientTaskInfoBean clientTaskInfoBean) {

		// 数据库增加记录
		String ip = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();;
		Long clientId = null;
		try {
			if(StringUtils.isEmpty(clientTaskInfoBean.getAppName()) || 
					StringUtils.isEmpty(clientTaskInfoBean.getClientTag()) || 
					StringUtils.isEmpty(clientTaskInfoBean.getTaskName())){
				throw new BaseException("clientTaskInfo error,AppName:{},ClientTag:{},TaskName:{}"
						,clientTaskInfoBean.getAppName(),clientTaskInfoBean.getClientTag(),clientTaskInfoBean.getTaskName());
			}
			CuckooClientJobDetailCriteria crt = new CuckooClientJobDetailCriteria();
			crt.createCriteria().andJobClassApplicationEqualTo(clientTaskInfoBean.getAppName())
			.andCuckooClientTagEqualTo(clientTaskInfoBean.getClientTag())
			.andJobNameEqualTo(clientTaskInfoBean.getTaskName());
			List<CuckooClientJobDetail> result = cuckooClientJobDetailMapper.selectByExample(crt);
			if(CollectionUtils.isEmpty(result)){
				
				CuckooClientJobDetail cuckooClientJobDetail = new CuckooClientJobDetail();
				cuckooClientJobDetail.setCuckooClientStatus(CuckooClientJobStatus.RUNNING.getValue());
				cuckooClientJobDetail.setCuckooClientTag(clientTaskInfoBean.getClientTag());
				cuckooClientJobDetail.setJobName(clientTaskInfoBean.getTaskName());
				cuckooClientJobDetail.setJobClassApplication(clientTaskInfoBean.getAppName());
				cuckooClientJobDetail.setBeanName(clientTaskInfoBean.getBeanName());
				cuckooClientJobDetail.setMethodName(clientTaskInfoBean.getMethodName());
				cuckooClientJobDetail.setCuckooClientIp(ip);
				cuckooClientJobDetail.setCreateDate(System.currentTimeMillis());
				cuckooClientJobDetailMapper.insertSelective(cuckooClientJobDetail);
				clientId =  cuckooClientJobDetailMapper.lastInsertId();
			}else{
				CuckooClientJobDetail cuckooClientJobDetail = result.get(0);
				clientId = cuckooClientJobDetail.getId();
				cuckooClientJobDetail.setCuckooClientStatus(CuckooClientJobStatus.RUNNING.getValue());
				cuckooClientJobDetailMapper.updateByPrimaryKeySelective(cuckooClientJobDetail);
			}
		} catch (Exception e) {
			
			LOGGER.error("falied add client job :{},clientTaskInfoBean:{}", e.getMessage(),clientTaskInfoBean, e);
		}
		
		// 链接缓存中增加缓存
		IoClientInfo socket = new IoClientInfo();
		socket.setRemoteApp(clientTaskInfoBean.getAppName());
		socket.setRemoteTag(clientTaskInfoBean.getClientTag());
		socket.setSession(session);
		
		JobClientSessionCache.put(clientId, socket);
		LOGGER.info("succed add client job ,clientTaskInfoBean:{}",clientTaskInfoBean);
		return clientId;
	}

}
