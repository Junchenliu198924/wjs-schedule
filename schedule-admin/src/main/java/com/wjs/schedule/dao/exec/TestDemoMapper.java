package com.wjs.schedule.dao.exec;

import com.wjs.schedule.domain.exec.TestDemo;
import com.wjs.schedule.domain.exec.TestDemoCriteria;
import com.wjs.util.dao.PageDataList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TestDemoMapper {
    /**
     * test_demo数据表的操作方法: countByExample  
     * 
     */
    int countByExample(TestDemoCriteria example);

    /**
     * test_demo数据表的操作方法: deleteByExample  
     * 
     */
    int deleteByExample(TestDemoCriteria example);

    /**
     * test_demo数据表的操作方法: deleteByPrimaryKey  
     * 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * test_demo数据表的操作方法: insert  
     * 
     */
    int insert(TestDemo record);

    /**
     * test_demo数据表的操作方法: insertSelective  
     * 
     */
    int insertSelective(TestDemo record);

    /**
     * test_demo数据表的操作方法: selectByExample  
     * 
     */
    List<TestDemo> selectByExample(TestDemoCriteria example);

    /**
     * test_demo数据表的操作方法: selectByPrimaryKey  
     * 
     */
    TestDemo selectByPrimaryKey(Long id);

    /**
     * test_demo数据表的操作方法: lockByPrimaryKey  
     * 
     */
    TestDemo lockByPrimaryKey(Long id);

    /**
     * test_demo数据表的操作方法: lockByExample  
     * 
     */
    TestDemo lockByExample(TestDemoCriteria example);

    /**
     * test_demo数据表的操作方法: pageByExample  
     * 
     */
    PageDataList<TestDemo> pageByExample(TestDemoCriteria example);

    /**
     * test_demo数据表的操作方法: lastInsertId  
     * 线程安全的获得当前连接，最近一个自增长主键的值（针对insert操作）
     * 使用last_insert_id()时要注意，当一次插入多条记录时(批量插入)，只是获得第一次插入的id值，务必注意。
     * 
     */
    Long lastInsertId();

    /**
     * test_demo数据表的操作方法: updateByExampleSelective  
     * 
     */
    int updateByExampleSelective(@Param("record") TestDemo record, @Param("example") TestDemoCriteria example);

    /**
     * test_demo数据表的操作方法: updateByExample  
     * 
     */
    int updateByExample(@Param("record") TestDemo record, @Param("example") TestDemoCriteria example);

    /**
     * test_demo数据表的操作方法: updateByPrimaryKeySelective  
     * 
     */
    int updateByPrimaryKeySelective(TestDemo record);

    /**
     * test_demo数据表的操作方法: updateByPrimaryKey  
     * 
     */
    int updateByPrimaryKey(TestDemo record);
}