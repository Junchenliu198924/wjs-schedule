package com.wjs.schedule.executor.framerwork;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.wjs.schedule.bean.ClientTaskInfoBean;
import com.wjs.schedule.constant.CuckooNetConstant;
import com.wjs.schedule.enums.MessageType;
import com.wjs.schedule.exception.BaseException;
import com.wjs.schedule.executor.annotation.CuckooTask;
import com.wjs.schedule.executor.aspectj.CuckooTaskAspect;
import com.wjs.schedule.executor.framerwork.bean.ClientInfoBean;
import com.wjs.schedule.executor.framerwork.bean.CuckooTaskBean;
import com.wjs.schedule.executor.framerwork.cache.CuckooTaskCache;
import com.wjs.schedule.net.client.handle.CuckooClientHandler;
import com.wjs.schedule.net.server.ServerUtil;
import com.wjs.schedule.net.server.bean.IoServerBean;
import com.wjs.schedule.net.server.cache.IoServerCollection;

public class CuckooClient implements ApplicationContextAware, BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(CuckooClient.class);

	private static ApplicationContext applicationContext; // Spring应用上下文环境
	
	private String clientTag;
	
	private String appName ;
	
	private String server;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		CuckooClient.applicationContext = applicationContext;
	}
	
	

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		ClientInfoBean.setAppName(appName);
		this.appName = appName;
	}

	public String getClientTag() {
		return clientTag;
	}



	public void setClientTag(String clientTag) {
		ClientInfoBean.setClientTag(clientTag);
		this.clientTag = clientTag;
	}



	public String getServer() {
		return server;
	}



	public void setServer(String server) {
		this.server = server;
	}



	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		if(null == bean){
			return null;
		}
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			Annotation[] anns = method.getAnnotations();
			if(null != anns){
				for (Annotation ann : anns) {
					if(ann.annotationType().equals(CuckooTask.class)){
						
						/*
						 * 1.扫描task注解，并且task.value不能为空，不能重复，否则报错
						 */
						CuckooTask task = (CuckooTask)ann;
						String taskName =  task.value();
						if(StringUtils.isEmpty(taskName)){
							throw new BaseException("cuckoo taskName can not be null,beanName:{},method:{} ",beanName , method.getName());
						}

						if(CuckooTaskCache.contains(taskName)){
							CuckooTaskBean taskBean = CuckooTaskCache.get(taskName);
							throw new BaseException("duplicate taskName,beanName1:{},method1:{} ,beanName2:{},method2:{}",beanName , method.getName(),taskBean.getBeanName() , taskBean.getMethodName()); 
						}
						// 在task注解上面，动态增加aspectj
						AspectJProxyFactory factory = new AspectJProxyFactory(bean);  
						factory.addAspect(CuckooTaskAspect.class);  
						bean =  factory.getProxy();  
						
						// 判断增加了CuckooTask注解的，需要增加到缓存里面
						CuckooTaskBean taskBean = new CuckooTaskBean();
						taskBean.setBeanName(beanName);
						taskBean.setMethodName(method.getName());
						taskBean.setTaskName(taskName);
						CuckooTaskCache.put(taskName, taskBean);

						
						
						LOGGER.info("init cuckooclient beanName:{},method:{},taskName:", beanName, method.getName(), task.value());
						System.out.println("beanName:"+beanName+",method:"+method.getName()+",annotation:"+ann);
					}
					
				}
			}
			
		}
		
		return bean;
	}



	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		LOGGER.info("Spring容器加载完成触发,开始连接服务器，并将客户端信息发送到服务器端进行注册,server:{},clientTag:{}", server , clientTag);
//		CuckooTaskCache
		if(StringUtils.isEmpty(server)){
			LOGGER.error("server config is null,Cuckoo will not start");
			return ;
		}
		server = StringUtils.trim(server);
//		1.可能有多个服务器，多个服务器如果一开始连接不上，需要重连
		String[] serverArrs = server.split(",");
		for (String serverStr : serverArrs) {
			
			String[] serverArr = serverStr.split(":");
			IoServerBean bean = new IoServerBean();
			bean.setIp(serverArr[0]);
			bean.setPort(Integer.valueOf(serverArr[1]));
			IoServerCollection.add(bean);
		}
		
		ServerUtil.retryConnect();
		
		// 发送客户端注册消息

		// 连接创建后，需要将客户端的task（clienInfoBean）注解发送给服务器
		if(CollectionUtils.isNotEmpty(CuckooTaskCache.getCache())){
			for (Iterator<CuckooTaskBean> it = CuckooTaskCache.getCache().iterator(); it.hasNext() ;) {
				CuckooTaskBean taskBean = it.next();
				ClientTaskInfoBean taskInfo =  new ClientTaskInfoBean();
				taskInfo.setAppName(ClientInfoBean.getAppName());
				taskInfo.setClientTag(ClientInfoBean.getClientTag());
				taskInfo.setBeanName(taskBean.getBeanName());
				taskInfo.setMethodName(taskBean.getMethodName());
				taskInfo.setTaskName(taskBean.getTaskName());
				ServerUtil.send(MessageType.REGIST, taskInfo);
			}
		
		}
		
	}
	
	
 

}
