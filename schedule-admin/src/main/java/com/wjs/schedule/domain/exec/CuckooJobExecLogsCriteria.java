package com.wjs.schedule.domain.exec;

import java.util.ArrayList;
import java.util.List;

public class CuckooJobExecLogsCriteria {
    /**
     * cockoo_job_exec_logs表的操作属性:orderByClause
     * 
     */
    protected String orderByClause;

    /**
     * cockoo_job_exec_logs表的操作属性:start
     * 
     */
    protected int start;

    /**
     * cockoo_job_exec_logs表的操作属性:limit
     * 
     */
    protected int limit;

    /**
     * cockoo_job_exec_logs表的操作属性:distinct
     * 
     */
    protected boolean distinct;

    /**
     * cockoo_job_exec_logs表的操作属性:oredCriteria
     * 
     */
    protected List<Criteria> oredCriteria;

    /**
     * cockoo_job_exec_logs数据表的操作方法: CuckooJobExecLogsCriteria  
     * 
     */
    public CuckooJobExecLogsCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: setOrderByClause  
     * 
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: getOrderByClause  
     * 
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: setStart  
     * 
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: getStart  
     * 
     */
    public int getStart() {
        return start;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: setLimit  
     * 
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: getLimit  
     * 
     */
    public int getLimit() {
        return limit;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: setDistinct  
     * 
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: isDistinct  
     * 
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: getOredCriteria  
     * 
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: or  
     * 
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: or  
     * 
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: createCriteria  
     * 
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: createCriteriaInternal  
     * 
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * cockoo_job_exec_logs数据表的操作方法: clear  
     * 
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * cockoo_job_exec_logs表的操作类
     * 
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andJobIdIsNull() {
            addCriterion("job_id is null");
            return (Criteria) this;
        }

        public Criteria andJobIdIsNotNull() {
            addCriterion("job_id is not null");
            return (Criteria) this;
        }

        public Criteria andJobIdEqualTo(Long value) {
            addCriterion("job_id =", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotEqualTo(Long value) {
            addCriterion("job_id <>", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThan(Long value) {
            addCriterion("job_id >", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdGreaterThanOrEqualTo(Long value) {
            addCriterion("job_id >=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThan(Long value) {
            addCriterion("job_id <", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdLessThanOrEqualTo(Long value) {
            addCriterion("job_id <=", value, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdIn(List<Long> values) {
            addCriterion("job_id in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotIn(List<Long> values) {
            addCriterion("job_id not in", values, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdBetween(Long value1, Long value2) {
            addCriterion("job_id between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andJobIdNotBetween(Long value1, Long value2) {
            addCriterion("job_id not between", value1, value2, "jobId");
            return (Criteria) this;
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNull() {
            addCriterion("group_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNotNull() {
            addCriterion("group_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupIdEqualTo(Long value) {
            addCriterion("group_id =", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotEqualTo(Long value) {
            addCriterion("group_id <>", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThan(Long value) {
            addCriterion("group_id >", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThanOrEqualTo(Long value) {
            addCriterion("group_id >=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThan(Long value) {
            addCriterion("group_id <", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThanOrEqualTo(Long value) {
            addCriterion("group_id <=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdIn(List<Long> values) {
            addCriterion("group_id in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotIn(List<Long> values) {
            addCriterion("group_id not in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdBetween(Long value1, Long value2) {
            addCriterion("group_id between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotBetween(Long value1, Long value2) {
            addCriterion("group_id not between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationIsNull() {
            addCriterion("job_class_application is null");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationIsNotNull() {
            addCriterion("job_class_application is not null");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationEqualTo(String value) {
            addCriterion("job_class_application =", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationNotEqualTo(String value) {
            addCriterion("job_class_application <>", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationGreaterThan(String value) {
            addCriterion("job_class_application >", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationGreaterThanOrEqualTo(String value) {
            addCriterion("job_class_application >=", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationLessThan(String value) {
            addCriterion("job_class_application <", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationLessThanOrEqualTo(String value) {
            addCriterion("job_class_application <=", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationLike(String value) {
            addCriterion("job_class_application like", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationNotLike(String value) {
            addCriterion("job_class_application not like", value, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationIn(List<String> values) {
            addCriterion("job_class_application in", values, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationNotIn(List<String> values) {
            addCriterion("job_class_application not in", values, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationBetween(String value1, String value2) {
            addCriterion("job_class_application between", value1, value2, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassApplicationNotBetween(String value1, String value2) {
            addCriterion("job_class_application not between", value1, value2, "jobClassApplication");
            return (Criteria) this;
        }

        public Criteria andJobClassNameIsNull() {
            addCriterion("job_class_name is null");
            return (Criteria) this;
        }

        public Criteria andJobClassNameIsNotNull() {
            addCriterion("job_class_name is not null");
            return (Criteria) this;
        }

        public Criteria andJobClassNameEqualTo(String value) {
            addCriterion("job_class_name =", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameNotEqualTo(String value) {
            addCriterion("job_class_name <>", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameGreaterThan(String value) {
            addCriterion("job_class_name >", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameGreaterThanOrEqualTo(String value) {
            addCriterion("job_class_name >=", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameLessThan(String value) {
            addCriterion("job_class_name <", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameLessThanOrEqualTo(String value) {
            addCriterion("job_class_name <=", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameLike(String value) {
            addCriterion("job_class_name like", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameNotLike(String value) {
            addCriterion("job_class_name not like", value, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameIn(List<String> values) {
            addCriterion("job_class_name in", values, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameNotIn(List<String> values) {
            addCriterion("job_class_name not in", values, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameBetween(String value1, String value2) {
            addCriterion("job_class_name between", value1, value2, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andJobClassNameNotBetween(String value1, String value2) {
            addCriterion("job_class_name not between", value1, value2, "jobClassName");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIsNull() {
            addCriterion("trigger_type is null");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIsNotNull() {
            addCriterion("trigger_type is not null");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeEqualTo(String value) {
            addCriterion("trigger_type =", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotEqualTo(String value) {
            addCriterion("trigger_type <>", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeGreaterThan(String value) {
            addCriterion("trigger_type >", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeGreaterThanOrEqualTo(String value) {
            addCriterion("trigger_type >=", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLessThan(String value) {
            addCriterion("trigger_type <", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLessThanOrEqualTo(String value) {
            addCriterion("trigger_type <=", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLike(String value) {
            addCriterion("trigger_type like", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotLike(String value) {
            addCriterion("trigger_type not like", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIn(List<String> values) {
            addCriterion("trigger_type in", values, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotIn(List<String> values) {
            addCriterion("trigger_type not in", values, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeBetween(String value1, String value2) {
            addCriterion("trigger_type between", value1, value2, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotBetween(String value1, String value2) {
            addCriterion("trigger_type not between", value1, value2, "triggerType");
            return (Criteria) this;
        }

        public Criteria andCronExpressionIsNull() {
            addCriterion("cron_expression is null");
            return (Criteria) this;
        }

        public Criteria andCronExpressionIsNotNull() {
            addCriterion("cron_expression is not null");
            return (Criteria) this;
        }

        public Criteria andCronExpressionEqualTo(String value) {
            addCriterion("cron_expression =", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotEqualTo(String value) {
            addCriterion("cron_expression <>", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionGreaterThan(String value) {
            addCriterion("cron_expression >", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionGreaterThanOrEqualTo(String value) {
            addCriterion("cron_expression >=", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionLessThan(String value) {
            addCriterion("cron_expression <", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionLessThanOrEqualTo(String value) {
            addCriterion("cron_expression <=", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionLike(String value) {
            addCriterion("cron_expression like", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotLike(String value) {
            addCriterion("cron_expression not like", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionIn(List<String> values) {
            addCriterion("cron_expression in", values, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotIn(List<String> values) {
            addCriterion("cron_expression not in", values, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionBetween(String value1, String value2) {
            addCriterion("cron_expression between", value1, value2, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotBetween(String value1, String value2) {
            addCriterion("cron_expression not between", value1, value2, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andTxDateIsNull() {
            addCriterion("tx_date is null");
            return (Criteria) this;
        }

        public Criteria andTxDateIsNotNull() {
            addCriterion("tx_date is not null");
            return (Criteria) this;
        }

        public Criteria andTxDateEqualTo(Integer value) {
            addCriterion("tx_date =", value, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateNotEqualTo(Integer value) {
            addCriterion("tx_date <>", value, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateGreaterThan(Integer value) {
            addCriterion("tx_date >", value, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateGreaterThanOrEqualTo(Integer value) {
            addCriterion("tx_date >=", value, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateLessThan(Integer value) {
            addCriterion("tx_date <", value, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateLessThanOrEqualTo(Integer value) {
            addCriterion("tx_date <=", value, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateIn(List<Integer> values) {
            addCriterion("tx_date in", values, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateNotIn(List<Integer> values) {
            addCriterion("tx_date not in", values, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateBetween(Integer value1, Integer value2) {
            addCriterion("tx_date between", value1, value2, "txDate");
            return (Criteria) this;
        }

        public Criteria andTxDateNotBetween(Integer value1, Integer value2) {
            addCriterion("tx_date not between", value1, value2, "txDate");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeIsNull() {
            addCriterion("job_start_time is null");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeIsNotNull() {
            addCriterion("job_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeEqualTo(Long value) {
            addCriterion("job_start_time =", value, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeNotEqualTo(Long value) {
            addCriterion("job_start_time <>", value, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeGreaterThan(Long value) {
            addCriterion("job_start_time >", value, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("job_start_time >=", value, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeLessThan(Long value) {
            addCriterion("job_start_time <", value, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeLessThanOrEqualTo(Long value) {
            addCriterion("job_start_time <=", value, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeIn(List<Long> values) {
            addCriterion("job_start_time in", values, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeNotIn(List<Long> values) {
            addCriterion("job_start_time not in", values, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeBetween(Long value1, Long value2) {
            addCriterion("job_start_time between", value1, value2, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobStartTimeNotBetween(Long value1, Long value2) {
            addCriterion("job_start_time not between", value1, value2, "jobStartTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeIsNull() {
            addCriterion("job_end_time is null");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeIsNotNull() {
            addCriterion("job_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeEqualTo(Long value) {
            addCriterion("job_end_time =", value, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeNotEqualTo(Long value) {
            addCriterion("job_end_time <>", value, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeGreaterThan(Long value) {
            addCriterion("job_end_time >", value, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("job_end_time >=", value, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeLessThan(Long value) {
            addCriterion("job_end_time <", value, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeLessThanOrEqualTo(Long value) {
            addCriterion("job_end_time <=", value, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeIn(List<Long> values) {
            addCriterion("job_end_time in", values, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeNotIn(List<Long> values) {
            addCriterion("job_end_time not in", values, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeBetween(Long value1, Long value2) {
            addCriterion("job_end_time between", value1, value2, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andJobEndTimeNotBetween(Long value1, Long value2) {
            addCriterion("job_end_time not between", value1, value2, "jobEndTime");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusIsNull() {
            addCriterion("exec_job_status is null");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusIsNotNull() {
            addCriterion("exec_job_status is not null");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusEqualTo(String value) {
            addCriterion("exec_job_status =", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusNotEqualTo(String value) {
            addCriterion("exec_job_status <>", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusGreaterThan(String value) {
            addCriterion("exec_job_status >", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusGreaterThanOrEqualTo(String value) {
            addCriterion("exec_job_status >=", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusLessThan(String value) {
            addCriterion("exec_job_status <", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusLessThanOrEqualTo(String value) {
            addCriterion("exec_job_status <=", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusLike(String value) {
            addCriterion("exec_job_status like", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusNotLike(String value) {
            addCriterion("exec_job_status not like", value, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusIn(List<String> values) {
            addCriterion("exec_job_status in", values, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusNotIn(List<String> values) {
            addCriterion("exec_job_status not in", values, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusBetween(String value1, String value2) {
            addCriterion("exec_job_status between", value1, value2, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecJobStatusNotBetween(String value1, String value2) {
            addCriterion("exec_job_status not between", value1, value2, "execJobStatus");
            return (Criteria) this;
        }

        public Criteria andExecClientIpIsNull() {
            addCriterion("exec_client_ip is null");
            return (Criteria) this;
        }

        public Criteria andExecClientIpIsNotNull() {
            addCriterion("exec_client_ip is not null");
            return (Criteria) this;
        }

        public Criteria andExecClientIpEqualTo(String value) {
            addCriterion("exec_client_ip =", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpNotEqualTo(String value) {
            addCriterion("exec_client_ip <>", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpGreaterThan(String value) {
            addCriterion("exec_client_ip >", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpGreaterThanOrEqualTo(String value) {
            addCriterion("exec_client_ip >=", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpLessThan(String value) {
            addCriterion("exec_client_ip <", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpLessThanOrEqualTo(String value) {
            addCriterion("exec_client_ip <=", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpLike(String value) {
            addCriterion("exec_client_ip like", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpNotLike(String value) {
            addCriterion("exec_client_ip not like", value, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpIn(List<String> values) {
            addCriterion("exec_client_ip in", values, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpNotIn(List<String> values) {
            addCriterion("exec_client_ip not in", values, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpBetween(String value1, String value2) {
            addCriterion("exec_client_ip between", value1, value2, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientIpNotBetween(String value1, String value2) {
            addCriterion("exec_client_ip not between", value1, value2, "execClientIp");
            return (Criteria) this;
        }

        public Criteria andExecClientPortIsNull() {
            addCriterion("exec_client_port is null");
            return (Criteria) this;
        }

        public Criteria andExecClientPortIsNotNull() {
            addCriterion("exec_client_port is not null");
            return (Criteria) this;
        }

        public Criteria andExecClientPortEqualTo(Integer value) {
            addCriterion("exec_client_port =", value, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortNotEqualTo(Integer value) {
            addCriterion("exec_client_port <>", value, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortGreaterThan(Integer value) {
            addCriterion("exec_client_port >", value, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("exec_client_port >=", value, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortLessThan(Integer value) {
            addCriterion("exec_client_port <", value, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortLessThanOrEqualTo(Integer value) {
            addCriterion("exec_client_port <=", value, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortIn(List<Integer> values) {
            addCriterion("exec_client_port in", values, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortNotIn(List<Integer> values) {
            addCriterion("exec_client_port not in", values, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortBetween(Integer value1, Integer value2) {
            addCriterion("exec_client_port between", value1, value2, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andExecClientPortNotBetween(Integer value1, Integer value2) {
            addCriterion("exec_client_port not between", value1, value2, "execClientPort");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeIsNull() {
            addCriterion("latest_check_time is null");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeIsNotNull() {
            addCriterion("latest_check_time is not null");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeEqualTo(Long value) {
            addCriterion("latest_check_time =", value, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeNotEqualTo(Long value) {
            addCriterion("latest_check_time <>", value, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeGreaterThan(Long value) {
            addCriterion("latest_check_time >", value, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("latest_check_time >=", value, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeLessThan(Long value) {
            addCriterion("latest_check_time <", value, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeLessThanOrEqualTo(Long value) {
            addCriterion("latest_check_time <=", value, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeIn(List<Long> values) {
            addCriterion("latest_check_time in", values, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeNotIn(List<Long> values) {
            addCriterion("latest_check_time not in", values, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeBetween(Long value1, Long value2) {
            addCriterion("latest_check_time between", value1, value2, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andLatestCheckTimeNotBetween(Long value1, Long value2) {
            addCriterion("latest_check_time not between", value1, value2, "latestCheckTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimesIsNull() {
            addCriterion("check_times is null");
            return (Criteria) this;
        }

        public Criteria andCheckTimesIsNotNull() {
            addCriterion("check_times is not null");
            return (Criteria) this;
        }

        public Criteria andCheckTimesEqualTo(Integer value) {
            addCriterion("check_times =", value, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesNotEqualTo(Integer value) {
            addCriterion("check_times <>", value, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesGreaterThan(Integer value) {
            addCriterion("check_times >", value, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesGreaterThanOrEqualTo(Integer value) {
            addCriterion("check_times >=", value, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesLessThan(Integer value) {
            addCriterion("check_times <", value, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesLessThanOrEqualTo(Integer value) {
            addCriterion("check_times <=", value, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesIn(List<Integer> values) {
            addCriterion("check_times in", values, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesNotIn(List<Integer> values) {
            addCriterion("check_times not in", values, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesBetween(Integer value1, Integer value2) {
            addCriterion("check_times between", value1, value2, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andCheckTimesNotBetween(Integer value1, Integer value2) {
            addCriterion("check_times not between", value1, value2, "checkTimes");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }
    }

    /**
     * cockoo_job_exec_logs表的操作类
     * 
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * cockoo_job_exec_logs表的操作类
     * 
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}