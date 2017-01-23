package com.wjs.schedule.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.wjs.schedule.vo.net.ClientInfo;

public class JobClientCache {
	
	/**
	 * 客户端连接缓存 Map<clientId,Socket>
	 */
	private static Map<Long, ClientInfo> channel = new ConcurrentHashMap<Long, ClientInfo>();
	
	private JobClientCache(){
		super();
	}
	
	public static ClientInfo get(Long clientId){
		
		return channel.get(clientId);
	}
	
	public static void put(Long clientId, ClientInfo socket){

		channel.put(clientId, socket);
	}
	
	public static void remove(Long clientId){

		channel.remove(clientId);
	}
	
	public static Long getClientIdBySession(IoSession session){
		
		if(session == null){
			return null;
		}
		for (Iterator<Entry<Long, ClientInfo>> it = channel.entrySet().iterator(); it.hasNext();) {
			Entry<Long, ClientInfo> entry = it.next();
			if(session.equals(entry.getValue())){
				return entry.getKey();
			}
		}
		return null;
	}
}
