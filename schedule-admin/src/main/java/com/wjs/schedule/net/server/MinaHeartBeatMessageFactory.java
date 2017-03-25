package com.wjs.schedule.net.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wjs.schedule.dao.exec.CuckooNetServerInfoMapper;
import com.wjs.schedule.domain.exec.CuckooNetServerInfo;
import com.wjs.schedule.domain.exec.CuckooNetServerInfoCriteria;
import com.wjs.schedule.enums.CuckooMessageType;

public class MinaHeartBeatMessageFactory implements KeepAliveMessageFactory {

	
	@Autowired
	CuckooNetServerInfoMapper cuckooNetServerInfoMapper;
	
	private static final Gson gson = new GsonBuilder().create();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MinaHeartBeatMessageFactory.class);
	public Object getRequest(IoSession session) {
		
//		LOGGER.info("getRequest" + session.getRemoteAddress()); // 客户端：10.138.254.70:53266
//		LOGGER.info("getRequest" + session.getServiceAddress()); // 服务端 ：0.0.0.0:10115
//		LOGGER.info("getRequest" + session.getLocalAddress()); // 服务端:10.138.254.10:10115

		LOGGER.info("request heart beat set: " + CuckooMessageType.HEARTBEATSERVER.getValue());
		InetSocketAddress serverAddr = (InetSocketAddress)session.getLocalAddress();
		CuckooNetServerInfoCriteria serverCrt = new CuckooNetServerInfoCriteria();
		serverCrt.createCriteria().andIpEqualTo(serverAddr.getHostName())
		.andPortEqualTo(serverAddr.getPort());
		
		CuckooNetServerInfo updateTime = new CuckooNetServerInfo();
		updateTime.setModifyDate(System.currentTimeMillis());
		
		cuckooNetServerInfoMapper.updateByExampleSelective(updateTime, serverCrt);
		
		return CuckooMessageType.HEARTBEATSERVER.getValue();
//		return gson.toJson(messageInfo);
	}

	public Object getResponse(IoSession session, Object request) {

		LOGGER.info("response heart beat set: " + CuckooMessageType.HEARTBEATCLIENT.getValue());
		

//		LOGGER.info("getResponse" + session.getRemoteAddress());
//		LOGGER.info("getResponse" + session.getServiceAddress());
//		LOGGER.info("getResponse" + session.getLocalAddress());
//		LOGGER.info("getResponse" + request);
		/** 返回预设语句 */
		return CuckooMessageType.HEARTBEATCLIENT.getValue();
	}

	public boolean isRequest(IoSession session, Object message) {
		


//		LOGGER.info("isRequest" + session.getRemoteAddress());
//		LOGGER.info("isRequest" + session.getServiceAddress());
//		LOGGER.info("isRequest" + session.getLocalAddress());
//		LOGGER.info("isRequest" + message);

		if (message.equals(CuckooMessageType.HEARTBEATSERVER.getValue())) {
			LOGGER.debug("request heart beat get : " + message.toString());
			// 服务端信息管理
			return true;
		}
		return false;
	}

	public boolean isResponse(IoSession session, Object message) {
		

//		LOGGER.info("isResponse" + session.getRemoteAddress());
//		LOGGER.info("isResponse" + session.getServiceAddress());
//		LOGGER.info("isResponse" + session.getLocalAddress());
//		LOGGER.info("isResponse" + message);
		
		if (message.equals(CuckooMessageType.HEARTBEATCLIENT.getValue())) {
			LOGGER.debug("response heart beat get : " + message.toString());
			return true;
		}
		return false;
	}
}