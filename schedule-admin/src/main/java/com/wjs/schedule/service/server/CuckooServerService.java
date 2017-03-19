package com.wjs.schedule.service.server;

import java.util.List;

import org.apache.mina.core.session.IoSession;

import com.wjs.schedule.bean.ClientTaskInfoBean;
import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.vo.job.CuckooClientJobExecResult;

public interface CuckooServerService {

	/**
	 * 调用远程任务
	 * @param jobBean
	 */
	CuckooClientJobExecResult execRemoteJob(List<CuckooClientJobDetail> remoteJobExecs, JobInfoBean jobBean);
	
	/**
	 * 查询可执行远程执行器列表 -- 考虑负载均衡 
	 * @param jobId
	 * @return
	 */
	List<CuckooClientJobDetail> getExecRemotesId(Long jobId);
	
	/**
	 * 新增可执行远程执行器
	 * @param taskInfo 
	 * @param session 
	 */
	Long addRemote(IoSession session, ClientTaskInfoBean taskInfoCuckooClientJobDetail);

}
