package com.wjs.schedule.net.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wjs.schedule.bean.JobInfoBean;
import com.wjs.schedule.bean.MessageInfo;
import com.wjs.schedule.component.cache.JobClientSessionCache;
import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.enums.CuckooMessageType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.vo.net.ClientInfo;

public class ServerUtil {


	private static final Logger LOGGER = LoggerFactory.getLogger(ServerUtil.class);
	private static final Gson gson = new GsonBuilder().create();
	
	public static void send(CuckooClientJobDetail socketClient, CuckooMessageType messageType, Object message) {
		
		ClientInfo clientInfo = JobClientSessionCache.get(socketClient.getId());
		if(null == clientInfo){
			LOGGER.info("could not get clientInfo by cuckooClient:{}", socketClient.getId());
			throw new BaseException("could not get clientInfo by cuckooClient:{}", socketClient.getId());
		}
		IoSession session = clientInfo.getSession();
		if(null == session){
			LOGGER.info("could not get session from cache , clientApp:{}, clientTag:{} ,cuckooClient:{}",clientInfo.getRemoteApp(), clientInfo.getRemoteTag(), socketClient.getId());
			throw new BaseException("could not get session from cache , clientApp:{}, clientTag:{} ,cuckooClient:{}",clientInfo.getRemoteApp(), clientInfo.getRemoteTag(), socketClient.getId());
		}
		
		
		// 给服务端发消息
		MessageInfo msgInfo = new MessageInfo();
		msgInfo.setMessage(message);
		msgInfo.setMessageType(messageType);
		String msg = gson.toJson(msgInfo);
		
		try {
			session.write(msg);
			LOGGER.info("server send message succed:server:{}, msg:{}",session.getServiceAddress(), msg);
		} catch (Exception e) {
			LOGGER.info("server send message failed:server:{}, msg:{},error:{}",session.getServiceAddress(), msg ,e.getMessage() ,e);
			throw new BaseException("server send message failed:server:{}, msg:{},error:{}",session.getServiceAddress(), msg ,e.getMessage());
		}
		
	}

}
