package com.wjs.schedule.executor.framerwork.scanner;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import com.wjs.schedule.executor.annotation.CuckooTask;

public class CuckooTaskScanner implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Map<String, Object> map = beanFactory.getBeansWithAnnotation(CuckooTask.class);
		for (String key : map.keySet()) {
			System.out.println("beanName= " + key);
		}
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

	}

}
