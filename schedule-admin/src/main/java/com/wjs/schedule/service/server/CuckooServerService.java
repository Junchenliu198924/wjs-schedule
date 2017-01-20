package com.wjs.schedule.service.server;

import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;

public interface CuckooServerService {

	/**
	 * 调用远程任务
	 * @param jobBean
	 */
	void execRemoteJob(CuckooClientJobDetail remoteJobExec, JobInfoBean jobBean);
	
	/**
	 * 查询远程执行器Id -- 考虑负载均衡 
	 * @param jobId
	 * @return
	 */
	CuckooClientJobDetail getExecRemoteId(Long jobId);

}
