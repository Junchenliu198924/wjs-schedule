package com.wjs.schedule.manage;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.wjs.schedule.component.cuckoo.CuckooJobCallBack;
import com.wjs.schedule.component.quartz.QuartzManage;
import com.wjs.schedule.constant.CuckooNetConstant;
import com.wjs.schedule.executor.framerwork.bean.ServerInfoBean;
import com.wjs.util.config.ConfigUtil;

public class CuckooContainerManager implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	QuartzManage quartzManage;

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooJobCallBack.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (event.getApplicationContext().getParent() == null) {
			// donothing 
			LOGGER.info("项目启动完成");
			
			try {
				ServerInfoBean.setIp(InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e) {
				LOGGER.error("get local Ip error:{}", e.getMessage() , e);
			}
			ServerInfoBean.setPort(ConfigUtil.getInteger(CuckooNetConstant.CUCKOO_SERVER_TCPPORT));
			
			quartzManage.addAutoJob();
		}
	}

	 
}
