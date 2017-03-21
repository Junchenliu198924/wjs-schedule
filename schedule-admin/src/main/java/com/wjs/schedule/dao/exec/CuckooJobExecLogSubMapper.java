package com.wjs.schedule.dao.exec;

import java.util.List;

import com.wjs.schedule.domain.exec.CuckooJobExecLog;
import com.wjs.schedule.vo.qry.JobLogOverTimeQry;

public interface CuckooJobExecLogSubMapper {

	Integer countOverTimeJobs(JobLogOverTimeQry qry);

	List<CuckooJobExecLog> pageOverTimeJobs(JobLogOverTimeQry qry);}