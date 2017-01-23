package com.wjs.schedule.dao.exec;

import com.wjs.schedule.vo.net.ClientInfo;

public interface CuckooClientJobDetailSubMapper {

	/**
	 * 服务器负载均衡控制.
	 * 1.按照【clientid】【clientTag】正在执行中的任务数量顺序排列。
	 * 2.并发任务，尽量不要跑到一台应用【clientTag】上面
	 * 	select s1.ip, s1.cuckoo_client_tag from cuckoo_client_job_detail s1
		where s1.job_id = 1
		and s1.cuckoo_client_status = 'DONE'
		group by s1.ip, s1.cuckoo_client_tag
		ORDER BY count(1) 
		limit 1
	 */
	ClientInfo getLoadBalanceClient(Long jobId);
}