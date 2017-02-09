package com.wjs.schedule.dao.exec;

import com.wjs.schedule.domain.exec.CuckooJobExecLogs;
import com.wjs.schedule.domain.exec.CuckooJobExecLogsCriteria;
import com.wjs.util.dao.PageDataList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuckooJobExecLogsMapper {
    /**
     * cuckoo_job_exec_logs数据表的操作方法: countByExample  
     * 
     */
    int countByExample(CuckooJobExecLogsCriteria example);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: deleteByExample  
     * 
     */
    int deleteByExample(CuckooJobExecLogsCriteria example);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: deleteByPrimaryKey  
     * 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: insert  
     * 
     */
    int insert(CuckooJobExecLogs record);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: insertSelective  
     * 
     */
    int insertSelective(CuckooJobExecLogs record);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: selectByExample  
     * 
     */
    List<CuckooJobExecLogs> selectByExample(CuckooJobExecLogsCriteria example);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: selectByPrimaryKey  
     * 
     */
    CuckooJobExecLogs selectByPrimaryKey(Long id);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: lockByPrimaryKey  
     * 
     */
    CuckooJobExecLogs lockByPrimaryKey(Long id);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: lockByExample  
     * 
     */
    CuckooJobExecLogs lockByExample(CuckooJobExecLogsCriteria example);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: pageByExample  
     * 
     */
    PageDataList<CuckooJobExecLogs> pageByExample(CuckooJobExecLogsCriteria example);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: lastInsertId  
     * 线程安全的获得当前连接，最近一个自增长主键的值（针对insert操作）
     * 使用last_insert_id()时要注意，当一次插入多条记录时(批量插入)，只是获得第一次插入的id值，务必注意。
     * 
     */
    Long lastInsertId();

    /**
     * cuckoo_job_exec_logs数据表的操作方法: updateByExampleSelective  
     * 
     */
    int updateByExampleSelective(@Param("record") CuckooJobExecLogs record, @Param("example") CuckooJobExecLogsCriteria example);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: updateByExample  
     * 
     */
    int updateByExample(@Param("record") CuckooJobExecLogs record, @Param("example") CuckooJobExecLogsCriteria example);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: updateByPrimaryKeySelective  
     * 
     */
    int updateByPrimaryKeySelective(CuckooJobExecLogs record);

    /**
     * cuckoo_job_exec_logs数据表的操作方法: updateByPrimaryKey  
     * 
     */
    int updateByPrimaryKey(CuckooJobExecLogs record);
}