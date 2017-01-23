package com.wjs.schedule.vo.net;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.mina.core.session.IoSession;

public class ClientInfo {
	
	/**
	 * IP
	 */
	private String remoteIp;
	
	/**
	 * tag
	 */
	private String remoteTag;
	
	/**
	 * 连接
	 */
	private IoSession session;

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
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
		
		return remoteIp.hashCode() | remoteTag.hashCode() ;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof ClientInfo)){  
            return false;  
        }  
     
		ClientInfo c =(ClientInfo)obj;  
   
        // ip和tag是相同表示同一个应用  
        return this.remoteIp.equals(c.remoteIp) && this.remoteTag.equals(c.remoteTag);  
	}

	@Override
	public String toString() {
		
		return ReflectionToStringBuilder.toString(this);
	}
}
