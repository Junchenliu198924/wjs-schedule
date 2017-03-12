package com.wjs.schedule.util;

import org.apache.commons.lang3.StringUtils;

import com.wjs.schedule.domain.exec.CuckooJobDependency;
import com.wjs.schedule.domain.exec.CuckooJobDetail;
import com.wjs.schedule.domain.exec.CuckooJobNextJob;
import com.wjs.schedule.enums.CuckooJobExecStatus;
import com.wjs.schedule.enums.CuckooJobStatus;
import com.wjs.schedule.vo.job.JobDependency;
import com.wjs.schedule.vo.job.CuckooJobDetailVo;
import com.wjs.schedule.vo.job.JobNext;

public class CuckBeanUtil {


	public static CuckooJobNextJob parseJobNext(JobNext jobNext) {

		CuckooJobNextJob cuckooJobNextJob = new CuckooJobNextJob();
		cuckooJobNextJob.setJobId(jobNext.getJobId());
		cuckooJobNextJob.setNextJobId(jobNext.getNextJobId());
		return cuckooJobNextJob;
	}

	public static CuckooJobDependency parseJobDependency(JobDependency jobDependency) {
		
		CuckooJobDependency cuckooJobDependency = new CuckooJobDependency();
		cuckooJobDependency.setJobId(jobDependency.getJobId());
		cuckooJobDependency.setDependencyJobId(jobDependency.getDependencyId());
		return cuckooJobDependency;
	}

}
