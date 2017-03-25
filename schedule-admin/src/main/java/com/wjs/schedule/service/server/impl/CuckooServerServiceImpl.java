package com.wjs.schedule.service.server.impl;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

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
import com.wjs.schedule.dao.exec.CuckooNetClientInfoMapper;
import com.wjs.schedule.dao.exec.CuckooNetClientJobMapMapper;
import com.wjs.schedule.dao.exec.CuckooNetRegistJobMapper;
import com.wjs.schedule.dao.exec.CuckooNetServerInfoMapper;
import com.wjs.schedule.dao.exec.CuckooNetServerJobMapMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooNetClientInfo;
import com.wjs.schedule.domain.exec.CuckooNetClientInfoCriteria;
import com.wjs.schedule.domain.exec.CuckooNetClientJobMap;
import com.wjs.schedule.domain.exec.CuckooNetClientJobMapCriteria;
import com.wjs.schedule.domain.exec.CuckooNetRegistJob;
import com.wjs.schedule.domain.exec.CuckooNetRegistJobCriteria;
import com.wjs.schedule.domain.exec.CuckooNetServerInfo;
import com.wjs.schedule.domain.exec.CuckooNetServerInfoCriteria;
import com.wjs.schedule.domain.exec.CuckooNetServerJobMap;
import com.wjs.schedule.enums.CuckooMessageType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.exception.JobCanNotRunningException;
import com.wjs.schedule.exception.JobRunningErrorException;
import com.wjs.schedule.net.server.ServerUtil;
import com.wjs.schedule.net.vo.IoClientInfo;
import com.wjs.schedule.service.job.CuckooJobService;
import com.wjs.schedule.service.server.CuckooServerService;
import com.wjs.schedule.vo.job.CuckooClientJobExecResult;
import com.wjs.util.bean.PropertyUtil;

@Service("cuckooServerService")
public class CuckooServerServiceImpl implements CuckooServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooServerServiceImpl.class);
	

	@Autowired
	CuckooJobService cuckooJobService;
	

	@Autowired
	CuckooNetClientInfoMapper cuckooNetClientInfoMapper;
	@Autowired
	CuckooNetClientJobMapMapper cuckooNetClientJobMapMapper;

	@Autowired
	CuckooNetRegistJobMapper cuckooNetRegistJobMapper;
	
	@Autowired
	CuckooNetServerJobMapMapper cuckooNetServerJobMapMapper;
	
	@Autowired
	CuckooNetServerInfoMapper cuckooNetServerInfoMapper;
	
	
	@Override
	public CuckooClientJobExecResult execRemoteJob(CuckooNetClientInfo cuckooNetClientInfo, JobInfoBean jobBean) throws JobCanNotRunningException, JobRunningErrorException {
		
		CuckooClientJobExecResult result = new CuckooClientJobExecResult();
		// 根据remoteJobExec 获取socket,
		IoClientInfo socket = JobClientSessionCache.get(cuckooNetClientInfo.getId());
		// 意外情况获取不到socket
		if(socket == null){
			throw new JobCanNotRunningException("JobClientSessionCache can not get socket");
		}
		
		// 更新远程服务器最新调用时间
		try {
			// socket写数据,触发客户端任务调度
			LOGGER.info("调用远程任务开始,jobApp:{},jobName:{},bean:{}" , cuckooNetClientInfo.getIp() ,cuckooNetClientInfo.getPort(), jobBean);
			ServerUtil.send(socket, CuckooMessageType.JOBDOING,  jobBean);
			result.setSuccess(true);
			return result;
		} catch (Exception e) {

			LOGGER.error("job exec error:{}",e.getMessage() ,e);
			throw new JobRunningErrorException("job exec error:{}",e.getMessage());
		}
		
	}

	@Override
	public CuckooNetClientInfo getExecNetClientInfo(Long jobId) {
		
		// 查看任务详细信息
		CuckooJobDetail jobInfo = cuckooJobService.getJobById(jobId);
		
		// 查询可执行服务器详细信息，并返回
		CuckooNetRegistJobCriteria registJobCrt = new CuckooNetRegistJobCriteria();
		registJobCrt.createCriteria().andJobClassApplicationEqualTo(jobInfo.getJobClassApplication())
		.andJobNameEqualTo(jobInfo.getJobName());
		List<CuckooNetRegistJob> registJobs = cuckooNetRegistJobMapper.selectByExample(registJobCrt);
		if(CollectionUtils.isNotEmpty(registJobs)){
			CuckooNetRegistJob cuckooNetRegistJob = registJobs.get(0);
			// 查询mapping数据
			CuckooNetClientJobMapCriteria clientMapCrt = new CuckooNetClientJobMapCriteria();
			clientMapCrt.createCriteria().andRegistIdEqualTo(cuckooNetRegistJob.getId());
			List<CuckooNetClientJobMap> clientMaps = cuckooNetClientJobMapMapper.selectByExample(clientMapCrt);
			if(CollectionUtils.isNotEmpty(clientMaps)){
				List<Long> clientIds = PropertyUtil.fetchFieldList(clientMaps, "clientId");
				CuckooNetClientInfoCriteria clientCrt = new CuckooNetClientInfoCriteria();
				clientCrt.createCriteria().andIdIn(clientIds);
				List<CuckooNetClientInfo> clientInfos = cuckooNetClientInfoMapper.selectByExample(clientCrt);
				if(CollectionUtils.isNotEmpty(clientInfos)){
					
					return clientInfos.get(new Random().nextInt(clientInfos.size()));
				}
			}
		}

		return null;
	}
	

	@Override
	@Transactional
	public Long addRemote(IoSession session, ClientTaskInfoBean clientTaskInfoBean) {

		
		if(StringUtils.isEmpty(clientTaskInfoBean.getAppName()) ||  StringUtils.isEmpty(clientTaskInfoBean.getTaskName())){
			throw new BaseException("clientTaskInfo error,AppName:{},TaskName:{}"
					,clientTaskInfoBean.getAppName(), clientTaskInfoBean.getTaskName());
		}
		
		// 判断是否增加 job_regist,Lock的方式
		CuckooNetRegistJobCriteria crtRegistJobLock = new CuckooNetRegistJobCriteria();
		crtRegistJobLock.createCriteria().andJobClassApplicationEqualTo(clientTaskInfoBean.getAppName())
		.andJobNameEqualTo(clientTaskInfoBean.getTaskName());
		CuckooNetRegistJob cuckooNetRegistJob = cuckooNetRegistJobMapper.lockByExample(crtRegistJobLock);
		if(null == cuckooNetRegistJob){
			// 新增一条数据，并且加锁
			cuckooNetRegistJob = new CuckooNetRegistJob();
			cuckooNetRegistJob.setJobClassApplication(clientTaskInfoBean.getAppName());
			cuckooNetRegistJob.setJobName(clientTaskInfoBean.getTaskName());
			cuckooNetRegistJob.setBeanName(clientTaskInfoBean.getBeanName());
			cuckooNetRegistJob.setMethodName(clientTaskInfoBean.getMethodName());
			cuckooNetRegistJob.setCreateDate(System.currentTimeMillis());
			cuckooNetRegistJob.setModifyDate(System.currentTimeMillis());
			try {
				cuckooNetRegistJobMapper.insertSelective(cuckooNetRegistJob);
				cuckooNetRegistJob.setId(cuckooNetRegistJobMapper.lastInsertId());
				cuckooNetRegistJob = cuckooNetRegistJobMapper.lockByPrimaryKey(cuckooNetRegistJob.getId());
			} catch (Exception e) {
				// 并发情况下，抛出异常，表示前面加锁失败了，后面再锁一次，忽略此处错误
				LOGGER.error("concurrent insert cuckooNetRegistJob error,appName:{},jobName:{}", clientTaskInfoBean.getAppName() , clientTaskInfoBean.getTaskName(), e);
				cuckooNetRegistJob = cuckooNetRegistJobMapper.lockByExample(crtRegistJobLock);
			}
			
		}

		// 增加 server_job关系
		
		
		InetSocketAddress serverSocket = (InetSocketAddress)session.getLocalAddress();
//		LOGGER.info("服务端IP："+ serverSocket.getHostName());
//		LOGGER.info("服务端Port："+ serverSocket.getPort());
		// 是否存在server
		CuckooNetServerInfoCriteria serverCrt =  new CuckooNetServerInfoCriteria();
		serverCrt.createCriteria().andIpEqualTo(serverSocket.getHostName())
		.andPortEqualTo(serverSocket.getPort());
		CuckooNetServerInfo cuckooNetServerInfo = cuckooNetServerInfoMapper.lockByExample(serverCrt);
		if(null == cuckooNetServerInfo){
			// 不存在，新增服务器
			cuckooNetServerInfo = new CuckooNetServerInfo();
			cuckooNetServerInfo.setIp(serverSocket.getHostName());
			cuckooNetServerInfo.setPort(serverSocket.getPort());
			cuckooNetServerInfo.setModifyDate(System.currentTimeMillis());
			try {
				cuckooNetServerInfoMapper.insertSelective(cuckooNetServerInfo);
				cuckooNetServerInfo.setId(cuckooNetServerInfoMapper.lastInsertId());
				cuckooNetServerInfo = cuckooNetServerInfoMapper.lockByPrimaryKey(cuckooNetServerInfo.getId());
			} catch (Exception e) {
				// 并发情况下，抛出异常，表示前面加锁失败了，后面再锁一次，忽略此处错误
				LOGGER.error("concurrent insert cuckooNetServerInfo error,ip:{},port:{}", serverSocket.getHostName(), serverSocket.getPort(), e);
				cuckooNetServerInfo = cuckooNetServerInfoMapper.lockByExample(serverCrt);
			}
			
		}
		
		// 增加registjob - server关联关系
		CuckooNetServerJobMap cuckooNetServerJobMap = new CuckooNetServerJobMap();
		cuckooNetServerJobMap.setRegistId(cuckooNetRegistJob.getId());
		cuckooNetServerJobMap.setServerId(cuckooNetServerInfo.getId());
		try {
			cuckooNetServerJobMapMapper.insertSelective(cuckooNetServerJobMap);
		} catch (Exception e) {
			// 唯一索引，无法插入的情况(控制好的话理论上不会出现，出现不影响)
			LOGGER.error("mapping serverinfo-registjob error,registJobId:{},serverInfoId:{} ", cuckooNetRegistJob.getId(), cuckooNetServerInfo.getId());
		}
		
		

		// 增加client_info 
		InetSocketAddress clientSocket = (InetSocketAddress)session.getRemoteAddress();
//		LOGGER.info("客户端IP："+ clientSocket.getHostName());
//		LOGGER.info("客户端Port："+ clientSocket.getPort());
		
		CuckooNetClientInfoCriteria clientCrt = new CuckooNetClientInfoCriteria();
		clientCrt.createCriteria().andIpEqualTo(clientSocket.getHostName())
		.andPortEqualTo(clientSocket.getPort());
		CuckooNetClientInfo cuckooNetClientInfo = cuckooNetClientInfoMapper.lockByExample(clientCrt);
		if(null == cuckooNetClientInfo){
			// 不存在改客户端信息，新增客户端
			cuckooNetClientInfo = new CuckooNetClientInfo();
			cuckooNetClientInfo.setIp(clientSocket.getHostName());
			cuckooNetClientInfo.setPort(clientSocket.getPort());
			try {
				cuckooNetClientInfoMapper.insertSelective(cuckooNetClientInfo);
				cuckooNetClientInfo.setId(cuckooNetClientInfoMapper.lastInsertId());
				cuckooNetClientInfo = cuckooNetClientInfoMapper.lockByPrimaryKey(cuckooNetClientInfo.getId());
			} catch (Exception e) {
				// 并发情况下，抛出异常，表示前面加锁失败了，后面再锁一次，忽略此处错误
				LOGGER.error("concurrent insert cuckooNetServerInfo error,ip:{},port:{}", serverSocket.getHostName(), serverSocket.getPort(), e);
				cuckooNetClientInfo = cuckooNetClientInfoMapper.lockByExample(clientCrt);
			}
		}
		
		// 增加client - job 的关系（同时维护一份内存与session的关系：一个client对应一个session，一个client对应多个regist）
		CuckooNetClientJobMap cuckooNetClientJobMap = new CuckooNetClientJobMap();
		cuckooNetClientJobMap.setClientId(cuckooNetClientInfo.getId());
		cuckooNetClientJobMap.setRegistId(cuckooNetRegistJob.getId());
		try {
			cuckooNetClientJobMapMapper.insertSelective(cuckooNetClientJobMap);
		} catch (Exception e1) {
			// 唯一索引，无法插入的情况(控制好的话理论上不会出现，出现不影响)
			LOGGER.error("mapping clientInfo-registjob error,registJobId:{},clientInfoId:{} ", cuckooNetRegistJob.getId(), cuckooNetClientInfo.getId());
		}
		
		// 链接缓存中增加缓存
		IoClientInfo socket = new IoClientInfo();
		socket.setIp(cuckooNetClientInfo.getIp());
		socket.setPort(cuckooNetClientInfo.getPort());
		socket.setSession(session);
		
		JobClientSessionCache.put(cuckooNetClientInfo.getId(), socket);
		LOGGER.info("succed add client job ,clientTaskInfoBean:{}",clientTaskInfoBean);
		return cuckooNetClientInfo.getId();
	}

}
