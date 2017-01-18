package com.wjs.schedule.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wjs.schedule.ServiceUnitBaseTest;
import com.wjs.schedule.dao.access.TestDemoMapper;

public class DaoInitTest extends ServiceUnitBaseTest{

	@Autowired
	TestDemoMapper mapper;
	
	@Test
	public void test(){

		System.out.println(mapper.selectByPrimaryKey(1L));
	}
}
