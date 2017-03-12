package com.wjs.schedule.constant;

public class CuckooJobConstant {
	
	/**
	 * 执行日志ID，用于quartz中的执行参数
	 */
	public static final String JOB_EXEC_ID = "job_exec_id";
	
	/**
	 * 是否强制启动任务
	 */
//	public static final String FORCE_JOB = "forceJob";
	/**
	 * 是否触发下级任务
	 */
	public static final String NEED_TRIGGLE_NEXT = "needTriggleNext";
	
	/**
	 * 日批Cron任务业务日期
	 */
	public static final String DAILY_JOB_TXDATE = "txDate";

	/**
	 * 流式任务开始时间
	 */
	public static final String FLOW_JOB_START_TIME = "flowJobStartTime";
	
	/**
	 * 流式任务结束时间
	 */
	public static final String FLOW_JOB_END_TIME = "flowJobEndTime";
	
	/**
	 * Cron表达式
	 */
//	public static final String QUARTZ_CRON_EXP = "quartzCronExpression";
	
	/**
	 * 等待任务锁名称
	 */
//	public static final String LOCK_NAME_PENDING_JOB = "lockNamePendingJob";
	
	public static final String QUARTZ_JOBNAME_JOINT = "_";
	
}
