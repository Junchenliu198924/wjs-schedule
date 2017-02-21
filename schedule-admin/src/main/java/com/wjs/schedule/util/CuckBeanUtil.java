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

	public static CuckooJobDetail parseJob(CuckooJobDetailVo jobInfo) {

		CuckooJobDetail job = new CuckooJobDetail();
		job.setId(jobInfo.getId());
		job.setCronExpression(jobInfo.getCronExpression());
		job.setCuckooParallelJobArgs(jobInfo.getCuckooParallelJobArgs());
		job.setGroupId(jobInfo.getGroupId());
		job.setJobClassApplication(jobInfo.getJobClassApplication());
		job.setJobDesc(jobInfo.getJobDesc());
		job.setJobStatus(StringUtils.isEmpty(jobInfo.getJobStatus()) ? CuckooJobStatus.PAUSE.getValue(): jobInfo.getJobStatus() );
		job.setJobName(jobInfo.getJobName());
		job.setNeedTriggleNext(true);
		job.setTypeDaily(jobInfo.getTypeDaily());
		job.setOffset(jobInfo.getOffset());
		job.setTriggerType(jobInfo.getTriggerType());
		job.setExecJobStatus(CuckooJobExecStatus.SUCCED.getValue());
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
