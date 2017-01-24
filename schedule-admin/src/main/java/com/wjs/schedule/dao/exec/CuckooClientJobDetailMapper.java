package com.wjs.schedule.dao.exec;

import com.wjs.schedule.domain.exec.CuckooClientJobDetail;
import com.wjs.schedule.domain.exec.CuckooClientJobDetailCriteria;
import com.wjs.util.dao.PageDataList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuckooClientJobDetailMapper {
    /**
     * cuckoo_client_job_detail数据表的操作方法: countByExample  
     * 
     */
    int countByExample(CuckooClientJobDetailCriteria example);

    /**
     * cuckoo_client_job_detail数据表的操作方法: deleteByExample  
     * 
     */
    int deleteByExample(CuckooClientJobDetailCriteria example);

    /**
     * cuckoo_client_job_detail数据表的操作方法: deleteByPrimaryKey  
     * 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * cuckoo_client_job_detail数据表的操作方法: insert  
     * 
     */
    int insert(CuckooClientJobDetail record);

    /**
     * cuckoo_client_job_detail数据表的操作方法: insertSelective  
     * 
     */
    int insertSelective(CuckooClientJobDetail record);

    /**
     * cuckoo_client_job_detail数据表的操作方法: selectByExample  
     * 
     */
    List<CuckooClientJobDetail> selectByExample(CuckooClientJobDetailCriteria example);

    /**
     * cuckoo_client_job_detail数据表的操作方法: selectByPrimaryKey  
     * 
     */
    CuckooClientJobDetail selectByPrimaryKey(Long id);

    /**
     * cuckoo_client_job_detail数据表的操作方法: lockByPrimaryKey  
     * 
     */
    CuckooClientJobDetail lockByPrimaryKey(Long id);

    /**
     * cuckoo_client_job_detail数据表的操作方法: lockByExample  
     * 
     */
    CuckooClientJobDetail lockByExample(CuckooClientJobDetailCriteria example);

    /**
     * cuckoo_client_job_detail数据表的操作方法: pageByExample  
     * 
     */
    PageDataList<CuckooClientJobDetail> pageByExample(CuckooClientJobDetailCriteria example);

    /**
     * cuckoo_client_job_detail数据表的操作方法: lastInsertId  
     * 线程安全的获得当前连接，最近一个自增长主键的值（针对insert操作）
     * 使用last_insert_id()时要注意，当一次插入多条记录时(批量插入)，只是获得第一次插入的id值，务必注意。
     * 
     */
    Long lastInsertId();

    /**
     * cuckoo_client_job_detail数据表的操作方法: updateByExampleSelective  
     * 
     */
    int updateByExampleSelective(@Param("record") CuckooClientJobDetail record, @Param("example") CuckooClientJobDetailCriteria example);

    /**
     * cuckoo_client_job_detail数据表的操作方法: updateByExample  
     * 
     */
    int updateByExample(@Param("record") CuckooClientJobDetail record, @Param("example") CuckooClientJobDetailCriteria example);

    /**
     * cuckoo_client_job_detail数据表的操作方法: updateByPrimaryKeySelective  
     * 
     */
    int updateByPrimaryKeySelective(CuckooClientJobDetail record);

    /**
     * cuckoo_client_job_detail数据表的操作方法: updateByPrimaryKey  
     * 
     */
    int updateByPrimaryKey(CuckooClientJobDetail record);
}