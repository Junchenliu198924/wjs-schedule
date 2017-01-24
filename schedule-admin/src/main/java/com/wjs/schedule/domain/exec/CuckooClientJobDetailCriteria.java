package com.wjs.schedule.domain.exec;

import java.util.ArrayList;
import java.util.List;

public class CuckooClientJobDetailCriteria {
    /**
     * cuckoo_client_job_detail表的操作属性:orderByClause
     * 
     */
    protected String orderByClause;

    /**
     * cuckoo_client_job_detail表的操作属性:start
     * 
     */
    protected int start;

    /**
     * cuckoo_client_job_detail表的操作属性:limit
     * 
     */
    protected int limit;

    /**
     * cuckoo_client_job_detail表的操作属性:distinct
     * 
     */
    protected boolean distinct;

    /**
     * cuckoo_client_job_detail表的操作属性:oredCriteria
     * 
     */
    protected List<Criteria> oredCriteria;

    /**
     * cuckoo_client_job_detail数据表的操作方法: CuckooClientJobDetailCriteria  
     * 
     */
    public CuckooClientJobDetailCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: setOrderByClause  
     * 
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: getOrderByClause  
     * 
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: setStart  
     * 
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: getStart  
     * 
     */
    public int getStart() {
        return start;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: setLimit  
     * 
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: getLimit  
     * 
     */
    public int getLimit() {
        return limit;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: setDistinct  
     * 
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: isDistinct  
     * 
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: getOredCriteria  
     * 
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: or  
     * 
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: or  
     * 
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: createCriteria  
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
     * cuckoo_client_job_detail数据表的操作方法: createCriteriaInternal  
     * 
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * cuckoo_client_job_detail数据表的操作方法: clear  
     * 
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * cuckoo_client_job_detail表的操作类
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

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
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

        public Criteria andCuckooClientTagIsNull() {
            addCriterion("cuckoo_client_tag is null");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagIsNotNull() {
            addCriterion("cuckoo_client_tag is not null");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagEqualTo(String value) {
            addCriterion("cuckoo_client_tag =", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagNotEqualTo(String value) {
            addCriterion("cuckoo_client_tag <>", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagGreaterThan(String value) {
            addCriterion("cuckoo_client_tag >", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagGreaterThanOrEqualTo(String value) {
            addCriterion("cuckoo_client_tag >=", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagLessThan(String value) {
            addCriterion("cuckoo_client_tag <", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagLessThanOrEqualTo(String value) {
            addCriterion("cuckoo_client_tag <=", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagLike(String value) {
            addCriterion("cuckoo_client_tag like", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagNotLike(String value) {
            addCriterion("cuckoo_client_tag not like", value, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagIn(List<String> values) {
            addCriterion("cuckoo_client_tag in", values, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagNotIn(List<String> values) {
            addCriterion("cuckoo_client_tag not in", values, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagBetween(String value1, String value2) {
            addCriterion("cuckoo_client_tag between", value1, value2, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientTagNotBetween(String value1, String value2) {
            addCriterion("cuckoo_client_tag not between", value1, value2, "cuckooClientTag");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusIsNull() {
            addCriterion("cuckoo_client_status is null");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusIsNotNull() {
            addCriterion("cuckoo_client_status is not null");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusEqualTo(String value) {
            addCriterion("cuckoo_client_status =", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusNotEqualTo(String value) {
            addCriterion("cuckoo_client_status <>", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusGreaterThan(String value) {
            addCriterion("cuckoo_client_status >", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusGreaterThanOrEqualTo(String value) {
            addCriterion("cuckoo_client_status >=", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusLessThan(String value) {
            addCriterion("cuckoo_client_status <", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusLessThanOrEqualTo(String value) {
            addCriterion("cuckoo_client_status <=", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusLike(String value) {
            addCriterion("cuckoo_client_status like", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusNotLike(String value) {
            addCriterion("cuckoo_client_status not like", value, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusIn(List<String> values) {
            addCriterion("cuckoo_client_status in", values, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusNotIn(List<String> values) {
            addCriterion("cuckoo_client_status not in", values, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusBetween(String value1, String value2) {
            addCriterion("cuckoo_client_status between", value1, value2, "cuckooClientStatus");
            return (Criteria) this;
        }

        public Criteria andCuckooClientStatusNotBetween(String value1, String value2) {
            addCriterion("cuckoo_client_status not between", value1, value2, "cuckooClientStatus");
            return (Criteria) this;
        }
    }

    /**
     * cuckoo_client_job_detail表的操作类
     * 
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * cuckoo_client_job_detail表的操作类
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