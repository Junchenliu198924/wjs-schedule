package com.wjs.schedule.service.job;

import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.vo.qry.JobClientQry;
import com.wjs.util.dao.PageDataList;

public interface CuckooClientJobDetailService {

	PageDataList<CuckooClientJobDetail> pageData(JobClientQry qry);

}
