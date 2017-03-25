package com.wjs.schedule.net.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wjs.schedule.constant.CuckooNetConstant;

public class MinaHeartBeatMessageFactory implements KeepAliveMessageFactory {

	private static final Gson gson = new GsonBuilder().create();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MinaHeartBeatMessageFactory.class);
	public Object getRequest(IoSession session) {
		
		LOGGER.info("getRequest" + session.getRemoteAddress());
		LOGGER.info("getRequest" + session.getServiceAddress());
		LOGGER.info("getRequest" + session.getLocalAddress());

		LOGGER.debug("request heart beat set: " + CuckooNetConstant.MINA_HEARTBEAT_MSG_SERVER);
		
//		MessageInfo messageInfo = new MessageInfo();
//		messageInfo.setMessageType(CuckooMessageType.HEARTBEATSERVER);
//		IoServerBean serverBean = new IoServerBean();
//		try {
//			serverBean.setIp(InetAddress.getLocalHost().getHostAddress());
//		} catch (UnknownHostException e) {
//			LOGGER.error("get local Ip error:{}", e.getMessage() , e);
//		}
//		serverBean.setPort(ConfigUtil.getInteger(CuckooNetConstant.CUCKOO_SERVER_TCPPORT));
//		messageInfo.setMessage(serverBean);
		
		return CuckooNetConstant.MINA_HEARTBEAT_MSG_SERVER;
//		return gson.toJson(messageInfo);
	}

	public Object getResponse(IoSession session, Object request) {

		LOGGER.debug("response heart beat set: " + CuckooNetConstant.MINA_HEARTBEAT_MSG_CLIENT);
		

		LOGGER.info("getResponse" + session.getRemoteAddress());
		LOGGER.info("getResponse" + session.getServiceAddress());
		LOGGER.info("getResponse" + session.getLocalAddress());
		LOGGER.info("getResponse" + request);
		/** 返回预设语句 */
		return CuckooNetConstant.MINA_HEARTBEAT_MSG_CLIENT;
	}

	public boolean isRequest(IoSession session, Object message) {
		


		LOGGER.info("isRequest" + session.getRemoteAddress());
		LOGGER.info("isRequest" + session.getServiceAddress());
		LOGGER.info("isRequest" + session.getLocalAddress());
		LOGGER.info("isRequest" + message);

		if (message.equals(CuckooNetConstant.MINA_HEARTBEAT_MSG_SERVER)) {
			LOGGER.debug("request heart beat get : " + message.toString());
			// 服务端信息管理
			return true;
		}
		return false;
	}

	public boolean isResponse(IoSession session, Object message) {
		

		LOGGER.info("isResponse" + session.getRemoteAddress());
		LOGGER.info("isResponse" + session.getServiceAddress());
		LOGGER.info("isResponse" + session.getLocalAddress());
		LOGGER.info("isResponse" + message);
		
		if (message.equals(CuckooNetConstant.MINA_HEARTBEAT_MSG_CLIENT)) {
			LOGGER.debug("response heart beat get : " + message.toString());
			return true;
		}
		return false;
	}
}