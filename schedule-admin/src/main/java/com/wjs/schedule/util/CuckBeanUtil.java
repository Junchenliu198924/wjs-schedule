package com.wjs.schedule.util;

import com.wjs.schedule.domain.exec.CuckooJobDependency;
import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobNextJob;
import com.wjs.schedule.vo.job.JobDependency;
import com.wjs.schedule.vo.job.CuckooJobDetailsVo;
import com.wjs.schedule.vo.job.JobNext;

public class CuckBeanUtil {

	public static CuckooJobDetails parseJob(CuckooJobDetailsVo jobInfo) {

		CuckooJobDetails job = new CuckooJobDetails();
		job.setId(jobInfo.getId());
		job.setGroupId(jobInfo.getGroupId());
		job.setJobClassApplication(jobInfo.getJobClassApplication());
		job.setJobDesc(jobInfo.getJobDesc());
		job.setJobName(jobInfo.getJobName());
		job.setJobStatus(jobInfo.getJobStatus());
		job.setOffset(jobInfo.getOffset());
		job.setTriggerType(jobInfo.getTriggerType());
		return job;
	}

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
