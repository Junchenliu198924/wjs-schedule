 
CREATE TABLE test_demo
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '标准ID',
	uri                            varchar(100)    DEFAULT ''         NOT NULL	COMMENT 'http请求uri',
	access_count_max               int             DEFAULT 0          NOT NULL	COMMENT '最大请求数',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='测试用'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;

CREATE TABLE cockoo_job_exec_logs
(
	id                             bigint          DEFAULT 0          NOT NULL	COMMENT '标准ID',
	job_id                         bigint          NOT NULL AUTO_INCREMENT	COMMENT '任务ID',
	group_id                       bigint          DEFAULT 0          NOT NULL	COMMENT '分组ID',
	job_class_application          varchar(50)     DEFAULT ''         NOT NULL	COMMENT '作业执行应用名',
	job_class_name                 varchar(128)    DEFAULT ''         NOT NULL	COMMENT '作业执行远程类名称',
	trigger_type                   varchar(10)     DEFAULT ''         NOT NULL	COMMENT '触发类型',
	cron_expression                varchar(20)     DEFAULT ''         NOT NULL	COMMENT 'cron任务表达式',
	tx_date                        int             DEFAULT 0          NOT NULL	COMMENT '任务执行业务日期',
	job_start_time                 decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '任务开始时间',
	job_end_time                   decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '任务结束时间',
	exec_job_status                varchar(10)     DEFAULT ''         NOT NULL	COMMENT '执行状态',
	cuckoo_client_ip               varchar(30)     DEFAULT ''         NOT NULL	COMMENT '执行器IP',
	cuckoo_client_tag              varchar(128)    DEFAULT ''         NOT NULL	COMMENT '客户端标识',
	latest_check_time              decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '最近检查时间',
	check_times                    int             DEFAULT 0          NOT NULL	COMMENT '断线已检查次数',
	remark                         varchar(500)    DEFAULT ''         NOT NULL	COMMENT '备注',
PRIMARY KEY(job_id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='任务执行流水表'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;

CREATE TABLE cockoo_job_details
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '标准ID',
	group_id                       bigint          DEFAULT 0          NOT NULL	COMMENT '分组ID',
	job_name                       varchar(100)    DEFAULT ''         NOT NULL	COMMENT '任务名称',
	job_class_application          varchar(50)     DEFAULT ''         NOT NULL	COMMENT '作业执行应用名',
	job_class_name                 varchar(128)    DEFAULT ''         NOT NULL	COMMENT '作业执行远程类名称',
	job_desc                       varchar(500)    DEFAULT ''         NOT NULL	COMMENT '任务描述',
	trigger_type                   varchar(10)     DEFAULT ''         NOT NULL	COMMENT '触发类型',
	offset                         int             DEFAULT 0          NOT NULL	COMMENT '偏移量',
	job_status                     varchar(10)     DEFAULT ''         NOT NULL	COMMENT '任务状态',
	exec_job_status                varchar(10)     DEFAULT ''         NOT NULL	COMMENT '执行状态',
	cuckoo_parallel_job_args       varchar(256)    DEFAULT ''         NOT NULL	COMMENT '并发/集群任务参数',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='任务表'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;


CREATE TABLE cuckoo_job_dependency
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '标准ID',
	job_id                         bigint          DEFAULT 0          NOT NULL	COMMENT '任务ID',
	dependency_job_id              bigint          DEFAULT 0          NOT NULL	COMMENT '依赖任务ID',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='上级任务依赖表'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;

CREATE TABLE cuckoo_job_group
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '标准ID',
	group_name                     varchar(100)    DEFAULT ''         NOT NULL	COMMENT '分组名称',
	group_desc                     varchar(500)    DEFAULT ''         NOT NULL	COMMENT '分组描述',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='任务分组表'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;


CREATE TABLE cuckoo_job_next_job
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '标准ID',
	job_id                         bigint          DEFAULT 0          NOT NULL	COMMENT '任务ID',
	next_job_id                    bigint          DEFAULT 0          NOT NULL	COMMENT '下级任务ID',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='下级任务触发表'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;


CREATE TABLE cuckoo_client_job_detail
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '标准ID',
	job_class_application          varchar(50)     DEFAULT ''         NOT NULL	COMMENT '作业执行应用名',
	ip                             varchar(32)     DEFAULT ''         NOT NULL	COMMENT 'ip地址',
	job_id                         bigint          DEFAULT 0          NOT NULL	COMMENT '任务ID',
	cuckoo_client_tag              varchar(128)    DEFAULT ''         NOT NULL	COMMENT '客户端标识',
	cuckoo_client_status           varchar(10)     DEFAULT ''         NOT NULL	COMMENT '客户端状态',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='客户端任务注册表'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;




