package com.wjs.schedule.exception;

import org.slf4j.helpers.MessageFormatter;

public class JobDependencyException extends Throwable{

	private static final long serialVersionUID = 1L;

	public JobDependencyException() {
		super();
	}


	public JobDependencyException(String message) {
		super(message);
	}
	

	public JobDependencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


	public JobDependencyException(String message, Throwable cause) {
		super(message, cause);
	}



	public JobDependencyException(Throwable cause) {
		super(cause);
	}
	
	public JobDependencyException(String format, Object... arguments) {
		
		super(MessageFormatter.arrayFormat(format, arguments).getMessage());
	}
	
	public static void main(String[] args) {
		String[] arr = {"你好","哈哈"};
		System.out.println(MessageFormatter.arrayFormat("ninhao:{},haha:{}", arr).getMessage());
	}



}
