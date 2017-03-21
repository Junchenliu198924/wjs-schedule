package com.wjs.schedule.service.Job;

import java.util.List;

import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.vo.job.JobGroup;

public interface CuckooGroupService {
	
	/**
	 * 新增分组，返回新增分组自增长主键
	 * @param group
	 * @return primarykey
	 */
	public Long addGroup(JobGroup group);

	/**
	 * 查询所有分组信息
	 * @return
	 */
	public List<CuckooJobGroup> selectAllGroup();

	/**
	 * 根据分组ID查询分组
	 * @param groupId
	 * @return
	 */
	public CuckooJobGroup getGroupById(Long groupId);
	
}
