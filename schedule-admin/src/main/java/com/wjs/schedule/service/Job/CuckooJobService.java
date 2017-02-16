package com.wjs.schedule.service.Job;

import java.util.List;
import java.util.Map;

import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.vo.job.CuckooJobDetailVo;
import com.wjs.schedule.vo.qry.JobInfoQry;
import com.wjs.util.dao.PageDataList;

/**
 * 任务执行服务接口
 * @author Silver
 *
 */
public interface CuckooJobService {
	
	
	
	/**
	 * 新增一个任务,返回任务id
	 */
	public Long addJob(CuckooJobDetailVo jobInfo);
	

	/**
	 * 删除一个任务
	 */
	public void removeJob(Long id);
	
	/**
	 * 修改一个任务
	 */
	public void modifyJob(CuckooJobDetailVo jobInfo);
	
	
	
	/**
	 * 暂停一个任务
	 */
	public void pauseOnejob(Long id);
	
	/**
	 * 暂停所有任务
	 */
	public void pauseAllJob();
	
	/**
	 * 恢复一个任务
	 */
	public void resumeOnejob(Long id);
	
	/**
	 * 恢复所有任务
	 */
	public void resumeAllJob();
	
	/**
	 * 将一个日切任务加入到待执行队列中
	 * @param id
	 * @param triggleNext
	 */
	public void pendingDailyJob(Long id,Boolean needTrigglerNext,Integer txDate);
	 
	/**
	 * 将一个非日切任务加入到待执行队列中
	 * @param id
	 * @param triggleNext
	 */
	public void pendingUnDailyJob(Long id,Boolean needTrigglerNext,Long startTime,Long endTime);
	
	

	/**
	 * 修改任务执行状态
	 * @param jobId
	 * @param succed
	 */
	public void updateJobStatusById(Long jobId, CuckooJobExecStatus succed);


	/**
	 * 根据ID查询任务明细
	 * @param jobId
	 * @return
	 */
	public CuckooJobDetail getJobById(Long jobId);


//	/**
//	 * 执行Debug任务
//	 */
//	public void tryTrigglePendingJob();


	/**
	 * 根据ID查询下级带触发任务
	 * @param jobId
	 * @return
	 */
	public List<CuckooJobDetail> getNextJobById(Long jobId);

	/**
	 * 分页查询任务数据
	 * @param jobInfo
	 * @param start
	 * @param length
	 * @return
	 */
	public PageDataList<CuckooJobDetail> pageList(JobInfoQry jobInfo, Integer start, Integer length);


	/**
	 * 查询所有客户端应用名称
	 * @return
	 */
	public Map<String,String> findAllApps();
	 
	
}
