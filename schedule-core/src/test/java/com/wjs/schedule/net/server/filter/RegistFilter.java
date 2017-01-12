package com.wjs.schedule.net.server.filter;

import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class RegistFilter extends IoFilterAdapter{
	
	

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
		System.out.println("============"+ session.getRemoteAddress());
		cause.printStackTrace();
		System.out.println("============");
		super.exceptionCaught(nextFilter, session, cause);
		
	}

	@Override
	public void onPostAdd(IoFilterChain parent, String name, NextFilter nextFilter) throws Exception {
		System.err.println("onPostAdd");
		super.onPostAdd(parent, name, nextFilter);
	}

	@Override
	public void onPostRemove(IoFilterChain parent, String name, NextFilter nextFilter) throws Exception {

		System.err.println("onPostRemove");
		super.onPostRemove(parent, name, nextFilter);
	}

	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {

		System.err.println("sessionCreated");
		super.sessionCreated(nextFilter, session);
	}

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {

		System.err.println("sessionOpened");
		super.sessionOpened(nextFilter, session);
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {

		System.err.println("sessionClosed");
		super.sessionClosed(nextFilter, session);
	}

	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {

		System.err.println("sessionIdle");
		super.sessionIdle(nextFilter, session, status);
	}

	
}
