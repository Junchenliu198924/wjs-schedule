package com.wjs.schedule.net.client.handle;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wjs.schedule.executor.framerwork.CuckooClient;
import com.wjs.schedule.net.client.ClientUtil;

public class CuckooClientHandler extends IoHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooClientHandler.class);
	Gson gson = new GsonBuilder().create();
    public void messageReceived(IoSession session, Object message) throws Exception {
        String content = message.toString();
        LOGGER.info("client receive a message is :{}" , content);
        
        ClientUtil.handleServerMessage(content);
        
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        // System.out.println("messageSent -> ：" + message);
    }

    
    
    
}