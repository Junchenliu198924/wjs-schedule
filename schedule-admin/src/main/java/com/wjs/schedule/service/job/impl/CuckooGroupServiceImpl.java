package com.wjs.schedule.service.job.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.dao.exec.CuckooJobDetailMapper;
import com.wjs.schedule.dao.exec.CuckooJobGroupMapper;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobDetailCriteria;
import com.wjs.schedule.domain.exec.CuckooJobGroup;
import com.wjs.schedule.domain.exec.CuckooJobGroupCriteria;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.service.job.CuckooGroupService;
import com.wjs.schedule.vo.job.JobGroup;
import com.wjs.util.bean.PropertyUtil;

@Service("cuckooGroupService")
public class CuckooGroupServiceImpl implements CuckooGroupService {

	@Autowired
	CuckooJobGroupMapper cuckooJobGroupMapper;
	
	@Autowired
	CuckooJobDetailMapper cuckooJobDetailMapper;
	
	@Override
	@Transactional
	public Long addGroup(JobGroup group) {
		
		if(null == group || StringUtils.isEmpty(group.getGroupName())){
			throw new BaseException("group name should not be null");
		}
		group.setId(null);
		CuckooJobGroup cuckGroup = new CuckooJobGroup();
		PropertyUtil.copyProperties(cuckGroup, group);
		cuckooJobGroupMapper.insertSelective(cuckGroup);
		return cuckooJobGroupMapper.lastInsertId();
	}

	@Override
	public List<CuckooJobGroup> selectAllGroup() {
		
		return cuckooJobGroupMapper.selectByExample(new CuckooJobGroupCriteria());
	}

	@Override
	public CuckooJobGroup getGroupById(Long groupId) {
		
		return cuckooJobGroupMapper.selectByPrimaryKey(groupId);
	}

	@Override
	public void deleteById(Long id) {

		cuckooJobGroupMapper.deleteByPrimaryKey(id);
		CuckooJobDetailCriteria crt = new CuckooJobDetailCriteria();
		crt.createCriteria().andGroupIdEqualTo(id);
		
		cuckooJobDetailMapper.deleteByExample(crt);
	}

}
