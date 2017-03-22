package com.wjs.schedule.net.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.wjs.schedule.constant.CuckooNetConstant;

public class MinaHeartBeatMessageFactory implements KeepAliveMessageFactory {

	private final Logger LOGGER = Logger.getLogger(MinaHeartBeatMessageFactory.class);

	public Object getRequest(IoSession session) {

		LOGGER.debug("request heart beat set: " + CuckooNetConstant.MINA_HEARTBEAT_MSG_SERVER);
		return CuckooNetConstant.MINA_HEARTBEAT_MSG_SERVER;
	}

	public Object getResponse(IoSession session, Object request) {

		LOGGER.debug("response heart beat set: " + CuckooNetConstant.MINA_HEARTBEAT_MSG_CLIENT);
		/** 返回预设语句 */
		return CuckooNetConstant.MINA_HEARTBEAT_MSG_CLIENT;
	}

	public boolean isRequest(IoSession session, Object message) {

		if (message.equals(CuckooNetConstant.MINA_HEARTBEAT_MSG_SERVER)) {
			LOGGER.debug("request heart beat get : " + message.toString());
			return true;
		}
		return false;
	}

	public boolean isResponse(IoSession session, Object message) {
		
		if (message.equals(CuckooNetConstant.MINA_HEARTBEAT_MSG_CLIENT)) {
			LOGGER.debug("response heart beat get : " + message.toString());
			return true;
		}
		return false;
	}
}