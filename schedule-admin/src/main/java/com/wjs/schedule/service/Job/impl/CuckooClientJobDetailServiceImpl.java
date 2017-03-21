package com.wjs.schedule.service.Job.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wjs.schedule.dao.exec.CuckooClientJobDetailMapper;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.domain.exec.CuckooClientJobDetailCriteria;
import com.wjs.schedule.service.Job.CuckooClientJobDetailService;
import com.wjs.schedule.vo.qry.JobClientQry;
import com.wjs.util.dao.PageDataList;

@Service("cuckooClientJobDetailService")
public class CuckooClientJobDetailServiceImpl implements CuckooClientJobDetailService{
	
	@Autowired
	CuckooClientJobDetailMapper cuckooClientJobDetailMapper;

	@Override
	public PageDataList<CuckooClientJobDetail> pageData(JobClientQry qry) {
	
		CuckooClientJobDetailCriteria crtPi = new CuckooClientJobDetailCriteria();
		crtPi.setStart(qry.getStart());
		crtPi.setLimit(qry.getLimit());
		crtPi.setOrderByClause(" bean_name, method_name ");
		CuckooClientJobDetailCriteria.Criteria crt = crtPi.createCriteria();
		if(StringUtils.isNotEmpty(qry.getJobClassApplication())){
			crt.andJobClassApplicationEqualTo(qry.getJobClassApplication());
		}
		
		return cuckooClientJobDetailMapper.pageByExample(crtPi);
	}

}
