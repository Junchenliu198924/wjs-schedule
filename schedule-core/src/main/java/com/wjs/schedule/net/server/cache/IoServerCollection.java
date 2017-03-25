package com.wjs.schedule.net.server.cache;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.wjs.schedule.net.vo.IoServerBean;

public class IoServerCollection {
	
	private static Set<IoServerBean> set = new HashSet<>();
	private IoServerCollection(){
		super();
	}
	
	public static boolean add(IoServerBean bean){
		return set.add(bean);
	}

	public static Set<IoServerBean> getSet() {
		return set;
	}

	public static void remove(InetSocketAddress clientAddr) {

		String ip = clientAddr.getAddress().getHostAddress();
		Integer port = clientAddr.getPort();
		remove(ip, port);
	}

	private static void remove(String ip, Integer port) {

		for (Iterator<IoServerBean> it = set.iterator(); it.hasNext() ;) {
			IoServerBean bean =  it.next();
			if(bean.getIp().equals(ip) && bean.getPort().equals(port)){
				it.remove();
			}
		}
		
	}
	
}
