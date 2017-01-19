package com.wjs.schedule.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wjs.schedule.ServiceUnitBaseTest;
import com.wjs.schedule.dao.exec.TestDemoMapper;
import com.wjs.schedule.domain.exec.TestDemo;

public class DaoInitTest extends ServiceUnitBaseTest{

	@Autowired
	TestDemoMapper mapper;
	
	@Test
	public void test(){
		mapper.insertSelective(new TestDemo(){
			private static final long serialVersionUID = 1L;
			{setUri("/url");}
		});
		System.out.println(mapper.selectByPrimaryKey(1L));
	}
}
