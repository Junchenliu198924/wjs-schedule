package com.wjs.schedule.dao.exec;

import com.wjs.schedule.domain.exec.CuckooLocks;
import com.wjs.schedule.domain.exec.CuckooLocksCriteria;
import com.wjs.util.dao.PageDataList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CuckooLocksMapper {
    /**
     * cuckoo_locks数据表的操作方法: countByExample  
     * 
     */
    int countByExample(CuckooLocksCriteria example);

    /**
     * cuckoo_locks数据表的操作方法: deleteByExample  
     * 
     */
    int deleteByExample(CuckooLocksCriteria example);

    /**
     * cuckoo_locks数据表的操作方法: deleteByPrimaryKey  
     * 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * cuckoo_locks数据表的操作方法: insert  
     * 
     */
    int insert(CuckooLocks record);

    /**
     * cuckoo_locks数据表的操作方法: insertSelective  
     * 
     */
    int insertSelective(CuckooLocks record);

    /**
     * cuckoo_locks数据表的操作方法: selectByExample  
     * 
     */
    List<CuckooLocks> selectByExample(CuckooLocksCriteria example);

    /**
     * cuckoo_locks数据表的操作方法: selectByPrimaryKey  
     * 
     */
    CuckooLocks selectByPrimaryKey(Long id);

    /**
     * cuckoo_locks数据表的操作方法: lockByPrimaryKey  
     * 
     */
    CuckooLocks lockByPrimaryKey(Long id);

    /**
     * cuckoo_locks数据表的操作方法: lockByExample  
     * 
     */
    CuckooLocks lockByExample(CuckooLocksCriteria example);

    /**
     * cuckoo_locks数据表的操作方法: pageByExample  
     * 
     */
    PageDataList<CuckooLocks> pageByExample(CuckooLocksCriteria example);

    /**
     * cuckoo_locks数据表的操作方法: lastInsertId  
     * 线程安全的获得当前连接，最近一个自增长主键的值（针对insert操作）
     * 使用last_insert_id()时要注意，当一次插入多条记录时(批量插入)，只是获得第一次插入的id值，务必注意。
     * 
     */
    Long lastInsertId();

    /**
     * cuckoo_locks数据表的操作方法: updateByExampleSelective  
     * 
     */
    int updateByExampleSelective(@Param("record") CuckooLocks record, @Param("example") CuckooLocksCriteria example);

    /**
     * cuckoo_locks数据表的操作方法: updateByExample  
     * 
     */
    int updateByExample(@Param("record") CuckooLocks record, @Param("example") CuckooLocksCriteria example);

    /**
     * cuckoo_locks数据表的操作方法: updateByPrimaryKeySelective  
     * 
     */
    int updateByPrimaryKeySelective(CuckooLocks record);

    /**
     * cuckoo_locks数据表的操作方法: updateByPrimaryKey  
     * 
     */
    int updateByPrimaryKey(CuckooLocks record);
}