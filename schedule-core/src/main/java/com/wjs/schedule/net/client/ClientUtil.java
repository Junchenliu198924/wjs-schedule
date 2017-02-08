package com.wjs.schedule.net.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.bean.MessageInfo;
import com.wjs.schedule.enums.MessageType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.executor.CuckooExecutor;
import com.wjs.schedule.executor.framerwork.bean.CuckooTaskBean;
import com.wjs.schedule.executor.framerwork.cache.CuckooTaskCache;

public class ClientUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientUtil.class);
	private static Gson gson = new GsonBuilder().create();
	
	
	public static void handleServerMessage(String message) {
		// TODO Auto-generated method stub
//		Map<MessageType, Object> retMap = gson.fromJson(message,  
//                new TypeToken<Map<MessageType, Object>>() {}.getType()); 
		MessageInfo msgInfo = gson.fromJson(message, MessageInfo.class);
		if(MessageType.JOBDOING.equals(msgInfo.getMessageType())){
			JobInfoBean jobInfo = (JobInfoBean)msgInfo.getMessage();
			
			CuckooTaskBean task = CuckooTaskCache.get(jobInfo.getJobName());
			if(null == task){
				LOGGER.error("can not find job by job name:{}, jobInfo:{}", jobInfo.getJobName(), jobInfo);
				throw new BaseException("can not find job by job name:{}, jobInfo:{}", jobInfo.getJobName(), jobInfo);
			}
			CuckooExecutor.exec(task, jobInfo);
		}else{
			LOGGER.error("unknow message type:{}", message);
			throw new BaseException("unknow message type:{}", message);
		}
	}

}
