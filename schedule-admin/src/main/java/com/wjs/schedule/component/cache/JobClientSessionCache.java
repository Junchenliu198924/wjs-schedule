package com.wjs.schedule.component.cache;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.wjs.schedule.vo.net.ClientInfo;

public class JobClientSessionCache {

	/**
	 * 客户端连接缓存 Map<clientId,Socket>
	 */
	private static ConcurrentHashMap<Long, ClientInfo> channel = new ConcurrentHashMap<Long, ClientInfo>();

	private JobClientSessionCache() {
		super();
	}

	public static ClientInfo get(Long clientId) {

		return channel.get(clientId);
	}

	public static void put(Long clientId, ClientInfo socket) {

		channel.put(clientId, socket);
	}

	public static void remove(Long clientId) {

		channel.remove(clientId);
	}

	public static void remove(IoSession session) {

		// 遍历缓存数据,由于可能有多台服务器，此处仅删除本机缓存。数据库记录信息不做处理
		for (Iterator<Entry<Long, ClientInfo>> it = channel.entrySet().iterator(); it.hasNext();) {
			Entry<Long, ClientInfo> entry = it.next();
			if (session.equals(entry.getValue().getSession())) {
				it.remove();
			}
		}
	}

}
