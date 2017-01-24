package com.wjs.schedule.executor.aspectj;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.wjs.schedule.executor.annotation.CuckooTask;

/**
 * mysql的for update no wait 实现
 * 参考资料：http://blog.itpub.net/7591490/viewspace-1033495/
 * @author Silver
 * @date 2017年1月15日 下午6:22:03 
 * 
 *
 */
@Aspect
@Component
public class CuckooTaskAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooTaskAspect.class);

	@Around("@annotation(task)")
	public Object lockWait(ProceedingJoinPoint pjp, CuckooTask task) throws Throwable {


		return pjp.proceed();

	}
}
