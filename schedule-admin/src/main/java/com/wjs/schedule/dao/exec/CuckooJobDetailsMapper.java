package com.wjs.schedule.dao.exec;

import com.wjs.schedule.domain.exec.CuckooJobDetails;
import com.wjs.schedule.domain.exec.CuckooJobDetailsCriteria;
import com.wjs.util.dao.PageDataList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuckooJobDetailsMapper {
    /**
     * cockoo_job_details数据表的操作方法: countByExample  
     * 
     */
    int countByExample(CuckooJobDetailsCriteria example);

    /**
     * cockoo_job_details数据表的操作方法: deleteByExample  
     * 
     */
    int deleteByExample(CuckooJobDetailsCriteria example);

    /**
     * cockoo_job_details数据表的操作方法: deleteByPrimaryKey  
     * 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * cockoo_job_details数据表的操作方法: insert  
     * 
     */
    int insert(CuckooJobDetails record);

    /**
     * cockoo_job_details数据表的操作方法: insertSelective  
     * 
     */
    int insertSelective(CuckooJobDetails record);

    /**
     * cockoo_job_details数据表的操作方法: selectByExample  
     * 
     */
    List<CuckooJobDetails> selectByExample(CuckooJobDetailsCriteria example);

    /**
     * cockoo_job_details数据表的操作方法: selectByPrimaryKey  
     * 
     */
    CuckooJobDetails selectByPrimaryKey(Long id);

    /**
     * cockoo_job_details数据表的操作方法: lockByPrimaryKey  
     * 
     */
    CuckooJobDetails lockByPrimaryKey(Long id);

    /**
     * cockoo_job_details数据表的操作方法: lockByExample  
     * 
     */
    CuckooJobDetails lockByExample(CuckooJobDetailsCriteria example);

    /**
     * cockoo_job_details数据表的操作方法: pageByExample  
     * 
     */
    PageDataList<CuckooJobDetails> pageByExample(CuckooJobDetailsCriteria example);

    /**
     * cockoo_job_details数据表的操作方法: lastInsertId  
     * 线程安全的获得当前连接，最近一个自增长主键的值（针对insert操作）
     * 使用last_insert_id()时要注意，当一次插入多条记录时(批量插入)，只是获得第一次插入的id值，务必注意。
     * 
     */
    Long lastInsertId();

    /**
     * cockoo_job_details数据表的操作方法: updateByExampleSelective  
     * 
     */
    int updateByExampleSelective(@Param("record") CuckooJobDetails record, @Param("example") CuckooJobDetailsCriteria example);

    /**
     * cockoo_job_details数据表的操作方法: updateByExample  
     * 
     */
    int updateByExample(@Param("record") CuckooJobDetails record, @Param("example") CuckooJobDetailsCriteria example);

    /**
     * cockoo_job_details数据表的操作方法: updateByPrimaryKeySelective  
     * 
     */
    int updateByPrimaryKeySelective(CuckooJobDetails record);

    /**
     * cockoo_job_details数据表的操作方法: updateByPrimaryKey  
     * 
     */
    int updateByPrimaryKey(CuckooJobDetails record);
}