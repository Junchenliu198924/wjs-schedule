package com.wjs.schedule.component.cuckoo;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wjs.schedule.bean.JobInfoBean;

/**
 * 客户端任务执行完成，调用方法
 * @author Silver
 *
 */
@Component("wjsCuckooJobCallBack")
public class CuckooJobCallBack  {

	@Transactional
	public void execJobCallBack(JobInfoBean jobBean){
		
		// TODO
		// 修改任务状态
		// 更新日志
		// 触发下级任务
	}

}
