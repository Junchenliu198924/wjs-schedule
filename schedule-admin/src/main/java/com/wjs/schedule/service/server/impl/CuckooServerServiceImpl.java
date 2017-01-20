package com.wjs.schedule.service.server.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.dao.exec.CuckooClientJobDetailMapper;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.domain.exec.CuckooClientJobDetailCriteria;
import com.wjs.schedule.enums.CuckooClientJobStatus;
import com.wjs.schedule.service.server.CuckooServerService;

@Service("cuckooServerService")
public class CuckooServerServiceImpl implements CuckooServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooServerServiceImpl.class);
	
	@Autowired
	CuckooClientJobDetailMapper cuckooClientJobDetailMapper;

	@Override
	public void execRemoteJob(CuckooClientJobDetail remoteJobExec, JobInfoBean jobBean) {
		
		
		// TODO
		
		// 根据remoteTag 获取socket
		
		// socket写数据
		
		LOGGER.info("调用远程任务开始:{}" + jobBean);
		System.out.println("执行远程任务：" + jobBean);
	}

	@Override
	public CuckooClientJobDetail getExecRemoteId(Long jobId) {
		
		// 查询可执行服务器
		CuckooClientJobDetailCriteria clientCrt = new CuckooClientJobDetailCriteria();
		clientCrt.createCriteria().andJobIdEqualTo(jobId)
		.andCuckooClientStatusEqualTo(CuckooClientJobStatus.RUNNING.getValue());
		List<CuckooClientJobDetail> cuckooClientJobDetails = cuckooClientJobDetailMapper.selectByExample(clientCrt);
		
		// TODO 服务器负载均衡控制.1.按照【clientid】【clientTag】正在执行中的任务数量顺序排列。2.并发任务，尽量不要跑到一台应用【clientid】【clientTag】上面
		// 
		return null;
	}

}
