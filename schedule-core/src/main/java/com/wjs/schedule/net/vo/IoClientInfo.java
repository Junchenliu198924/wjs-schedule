package com.wjs.schedule.net.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.mina.core.session.IoSession;

public class IoClientInfo {
	
	/**
	 * IP
	 */
	private String remoteApp;
	
	/**
	 * tag
	 */
	private String remoteTag;
	
	/**
	 * 连接 -- 每一个APP和Tag只有一个连接和当前服务器连接
	 */
	private  IoSession session;


	public String getRemoteApp() {
		return remoteApp;
	}

	public void setRemoteApp(String remoteApp) {
		this.remoteApp = remoteApp;
	}

	public String getRemoteTag() {
		return remoteTag;
	}

	public void setRemoteTag(String remoteTag) {
		this.remoteTag = remoteTag;
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
		result = prime * result + ((remoteApp == null) ? 0 : remoteApp.hashCode());
		result = prime * result + ((remoteTag == null) ? 0 : remoteTag.hashCode());
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
		if (remoteApp == null) {
			if (other.remoteApp != null)
				return false;
		} else if (!remoteApp.equals(other.remoteApp))
			return false;
		if (remoteTag == null) {
			if (other.remoteTag != null)
				return false;
		} else if (!remoteTag.equals(other.remoteTag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}

}
