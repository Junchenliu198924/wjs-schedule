package com.wjs.schedule.net.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.mina.core.session.IoSession;

public class IoClientInfo {
	
	/**
	 * IP
	 */
	private String ip;
	
	/**
	 * tag
	 */
	private Integer port;
	
	/**
	 * 连接 -- 每一个APP和Tag只有一个连接和当前服务器连接
	 */
	private  IoSession session;


	



	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	
	
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IoClientInfo other = (IoClientInfo) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}

}
