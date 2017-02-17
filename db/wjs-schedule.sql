CREATE TABLE cuckoo_client_job_detail
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	job_class_application          varchar(50)     DEFAULT ''         NOT NULL	COMMENT '��ҵִ��Ӧ����',
	cuckoo_client_ip               varchar(30)     DEFAULT ''         NOT NULL	COMMENT 'ִ����IP',
	cuckoo_client_tag              varchar(128)    DEFAULT ''         NOT NULL	COMMENT '�ͻ��˱�ʶ',
	cuckoo_client_status           varchar(10)     DEFAULT ''         NOT NULL	COMMENT '�ͻ���״̬',
	job_name                       varchar(100)    DEFAULT ''         NOT NULL	COMMENT '��������',
	bean_name                      varchar(256)    DEFAULT ''         NOT NULL	COMMENT 'ʵ��������',
	method_name                    varchar(100)    DEFAULT ''         NOT NULL	COMMENT '��������',
	create_date                    decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '����ʱ��',
	modify_date                    decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '�޸�ʱ��',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='�ͻ�������ע���'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;
CREATE UNIQUE INDEX uk_client_job ON cuckoo_client_job_detail(job_class_application ASC ,cuckoo_client_tag ASC ,job_name ASC );



CREATE TABLE cuckoo_job_dependency
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	job_id                         bigint          DEFAULT 0          NOT NULL	COMMENT '����ID',
	dependency_job_id              bigint          DEFAULT 0          NOT NULL	COMMENT '��������ID',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='�ϼ�����������'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;


CREATE TABLE cuckoo_job_detail
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	group_id                       bigint          DEFAULT 0          NOT NULL	COMMENT '����ID',
	job_name                       varchar(100)    DEFAULT ''         NOT NULL	COMMENT '��������',
	job_class_application          varchar(50)     DEFAULT ''         NOT NULL	COMMENT '��ҵִ��Ӧ����',
	job_desc                       varchar(500)    DEFAULT ''         NOT NULL	COMMENT '��������',
	trigger_type                   varchar(10)     DEFAULT ''         NOT NULL	COMMENT '��������',
	cron_expression                varchar(20)     DEFAULT ''         NOT NULL	COMMENT 'cron������ʽ',
	type_daily                     varchar(6)      DEFAULT ''         NOT NULL	COMMENT '�Ƿ�Ϊ��������',
	offset                         int             DEFAULT 0          NOT NULL	COMMENT 'ƫ����',
	job_status                     varchar(10)     DEFAULT ''         NOT NULL	COMMENT '����״̬',
	cuckoo_parallel_job_args       varchar(256)    DEFAULT ''         NOT NULL	COMMENT '����/��Ⱥ�������',
	exec_job_status                varchar(10)     DEFAULT ''         NOT NULL	COMMENT 'ִ��״̬',
	tx_date                        int             DEFAULT 0          NOT NULL	COMMENT '����ִ��ҵ������',
	flow_last_time                 decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '��ʽ������һ��ʱ�����',
	flow_cur_time                  decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '��ʽ����ǰʱ�����',
	need_triggle_next              boolean         DEFAULT 1          NOT NULL	COMMENT '�Ƿ񴥷��¼�����',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='�����'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;
CREATE UNIQUE INDEX uk_cuckoo_job_detail ON cuckoo_job_detail(group_id ASC ,job_name ASC );



CREATE TABLE cuckoo_job_exec_log
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	job_id                         bigint          DEFAULT 0          NOT NULL	COMMENT '����ID',
	group_id                       bigint          DEFAULT 0          NOT NULL	COMMENT '����ID',
	job_class_application          varchar(50)     DEFAULT ''         NOT NULL	COMMENT '��ҵִ��Ӧ����',
	job_class_name                 varchar(128)    DEFAULT ''         NOT NULL	COMMENT '��ҵִ��Զ��������',
	trigger_type                   varchar(10)     DEFAULT ''         NOT NULL	COMMENT '��������',
	cron_expression                varchar(20)     DEFAULT ''         NOT NULL	COMMENT 'cron������ʽ',
	tx_date                        int             DEFAULT 0          NOT NULL	COMMENT '����ִ��ҵ������',
	flow_last_time                 decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '��ʽ������һ��ʱ�����',
	flow_cur_time                  decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '��ʽ����ǰʱ�����',
	job_start_time                 decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '����ʼʱ��',
	job_end_time                   decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '�������ʱ��',
	exec_job_status                varchar(10)     DEFAULT ''         NOT NULL	COMMENT 'ִ��״̬',
	cuckoo_client_ip               varchar(30)     DEFAULT ''         NOT NULL	COMMENT 'ִ����IP',
	cuckoo_client_tag              varchar(128)    DEFAULT ''         NOT NULL	COMMENT '�ͻ��˱�ʶ',
	latest_check_time              decimal(13,0)   DEFAULT 0          NOT NULL	COMMENT '������ʱ��',
	need_triggle_next              boolean         DEFAULT 1          NOT NULL	COMMENT '�Ƿ񴥷��¼�����',
	remark                         varchar(500)    DEFAULT ''         NOT NULL	COMMENT '��ע',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='����ִ����ˮ��'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;



CREATE TABLE cuckoo_job_group
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	group_name                     varchar(100)    DEFAULT ''         NOT NULL	COMMENT '��������',
	group_desc                     varchar(500)    DEFAULT ''         NOT NULL	COMMENT '��������',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='��������'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;



CREATE TABLE cuckoo_job_next_job
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	job_id                         bigint          DEFAULT 0          NOT NULL	COMMENT '����ID',
	next_job_id                    bigint          DEFAULT 0          NOT NULL	COMMENT '�¼�����ID',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='�¼����񴥷���'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;
CREATE UNIQUE INDEX uk_cuckoo_next_job ON cuckoo_job_next_job(next_job_id ASC );



CREATE TABLE cuckoo_locks
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	lock_name                      varchar(64)     DEFAULT ''         NOT NULL	COMMENT '������',
	cuckoo_server_ip               varchar(30)     DEFAULT ''         NOT NULL	COMMENT '������IP',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='����ִ�����洢��'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;
CREATE UNIQUE INDEX uk_cuckoo_locks ON cuckoo_locks(lock_name ASC );


CREATE TABLE test_demo
(
	id                             bigint          NOT NULL AUTO_INCREMENT	COMMENT '��׼ID',
	uri                            varchar(100)    DEFAULT ''         NOT NULL	COMMENT 'http����uri',
	access_count_max               int             DEFAULT 0          NOT NULL	COMMENT '���������',
PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='������'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;



