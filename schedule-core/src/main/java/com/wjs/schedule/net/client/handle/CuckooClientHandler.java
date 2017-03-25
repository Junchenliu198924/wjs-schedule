package com.wjs.schedule.net.client.handle;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.bean.MessageInfo;
import com.wjs.schedule.constant.CuckooNetConstant;
import com.wjs.schedule.enums.CuckooMessageType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.executor.CuckooExecutor;
import com.wjs.schedule.executor.framerwork.CuckooClient;
import com.wjs.schedule.executor.framerwork.bean.ClientInfoBean;
import com.wjs.schedule.executor.framerwork.bean.CuckooTaskBean;
import com.wjs.schedule.executor.framerwork.cache.CuckooTaskCache;
import com.wjs.schedule.net.client.ClientUtil;

public class CuckooClientHandler extends IoHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooClientHandler.class);
	private static final Gson gson = new GsonBuilder().create();
    public void messageReceived(IoSession session, Object message) throws Exception {
        String content = message.toString();
        LOGGER.info("cuckoo client receive a message is :{}" , content);
        

		LOGGER.info("messageReceived" + session.getRemoteAddress());
		LOGGER.info("messageReceived" + session.getServiceAddress());
		LOGGER.info("messageReceived" + session.getLocalAddress());
		LOGGER.info("messageReceived" + message);
		LOGGER.info("messageReceived" + ClientInfoBean.getAppName());
		LOGGER.info("messageReceived" + ClientInfoBean.getClientTag());
        
        if (CuckooNetConstant.MINA_HEARTBEAT_MSG_SERVER.equals(content)) {
            // 收到心跳包
        	LOGGER.debug("heart_beat_message");
            session.write(CuckooNetConstant.MINA_HEARTBEAT_MSG_CLIENT);
            return;
        }

        MessageInfo msgInfo = null;
		try {
			msgInfo = gson.fromJson(content, MessageInfo.class);
		} catch (Exception e) {
			LOGGER.error("gson fromJson error, json:{}, error:{}", content, e.getMessage());
		}
		if(CuckooMessageType.JOBDOING.equals(msgInfo.getMessageType())){
			
			JobInfoBean jobInfo = gson.fromJson(gson.toJson(msgInfo.getMessage()), JobInfoBean.class);
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

    public void messageSent(IoSession session, Object message) throws Exception {
        // System.out.println("messageSent -> ：" + message);
    }

    
    
    
}