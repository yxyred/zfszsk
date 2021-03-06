----liukai,2019/11/15,专家-----
CREATE TABLE "LAW"."BUM_EXPERT_T"
(
"ID" VARCHAR(50) NOT NULL,
"NAME" VARCHAR(50),
"GENDER" VARCHAR(2),
"AREA" VARCHAR(50),
"POST" VARCHAR(200),
"UNIT_TYPE" VARCHAR(50),
"UNIT_TYPE_NAME" VARCHAR(200),
"UNIT_NAME" VARCHAR(200),
"EXPERTISE" VARCHAR(200),
"CERT" VARCHAR(18),
"LAW" VARCHAR(500),
"TEL" VARCHAR(20),
"CREATE_TIME" DATETIME(6),
"UPDATE_TIME" DATETIME(6),
"CREATOR_ID" VARCHAR(128),
"UPDATE_USER_ID" VARCHAR(128),
"DEL_FLAG" VARCHAR(2) DEFAULT 0,
NOT CLUSTER PRIMARY KEY("ID")) STORAGE(ON "MAIN", CLUSTERBTR) ;
COMMENT ON TABLE "LAW"."BUM_EXPERT_T" IS '专家库';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."AREA" IS '专家所属地区';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."CERT" IS '专家身份证号';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."CREATE_TIME" IS '创建时间';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."CREATOR_ID" IS '创建人ID';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."DEL_FLAG" IS '删除标志';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."EXPERTISE" IS '专家技术专长';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."GENDER" IS '性别';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."ID" IS '主键';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."LAW" IS '辅法经历';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."NAME" IS '专家姓名';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."POST" IS '专家职务';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."TEL" IS '专家电话号';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."UNIT_NAME" IS '工作单位';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."UNIT_TYPE" IS '专家工作单位类别';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."UNIT_TYPE_NAME" IS '专家工作单位类别名称';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."UPDATE_TIME" IS '更新时间';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_T"."UPDATE_USER_ID" IS '修改人员ID';
;
--------liukai,2019/11/18,专家辅法经历表-------------------------------
CREATE TABLE "LAW"."BUM_EXPERT_EXPERIENCE_T"
(
"ID" VARCHAR(50) NOT NULL,
"EXPERT_ID" VARCHAR(50) NOT NULL,
"EXPERIENCE" VARCHAR(50) NOT NULL,
"EXPERIENCE_TYPE" VARCHAR(50) NOT NULL,
"EXPERIENCE_TYPE_NAME" VARCHAR(200) NOT NULL,
"CREATE_TIME" DATETIME(6),
"UPDATE_TIME" DATETIME(6),
"CREATOR_ID" VARCHAR(128),
"UPDATE_USER_ID" VARCHAR(128),
"DEL_FLAG" VARCHAR(2) DEFAULT 0,
NOT CLUSTER PRIMARY KEY("ID")) STORAGE(ON "MAIN", CLUSTERBTR) ;
COMMENT ON TABLE "LAW"."BUM_EXPERT_EXPERIENCE_T" IS '专家辅法经历表';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."CREATE_TIME" IS '创建时间';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."CREATOR_ID" IS '创建人ID';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."DEL_FLAG" IS '删除标志';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."EXPERIENCE" IS '辅法经历';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."ID" IS '主键';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."EXPERT_ID" IS '关联专家表';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."EXPERIENCE_TYPE" IS '辅法类别';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."EXPERIENCE_TYPE_NAME" IS '辅法类别名称';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."UPDATE_TIME" IS '更新时间';
COMMENT ON COLUMN "LAW"."BUM_EXPERT_EXPERIENCE_T"."UPDATE_USER_ID" IS '修改人员ID';
;
--------刘凯，2019/11/15,用户token表---------
--------直接删除原表替换该表--------------
CREATE TABLE "LAW"."SYS_USER_TOKEN"
(
"USER_ID" VARCHAR(50) NOT NULL,
"TOKEN" VARCHAR(400) NOT NULL,
"EXPIRE_TIME" DATETIME(6),
"UPDATE_TIME" DATETIME(6),
"CREATE_TIME" DATETIME(6),
"CREATOR_ID" VARCHAR(50),
"UPDATE_USER_ID" VARCHAR(50),
"DEL_FLAG" VARCHAR(2) DEFAULT 0) STORAGE(ON "MAIN", CLUSTERBTR) ;
COMMENT ON TABLE "LAW"."SYS_USER_TOKEN" IS '系统用户Token';
COMMENT ON COLUMN "LAW"."SYS_USER_TOKEN"."EXPIRE_TIME" IS '过期时间';
COMMENT ON COLUMN "LAW"."SYS_USER_TOKEN"."TOKEN" IS 'token';
COMMENT ON COLUMN "LAW"."SYS_USER_TOKEN"."UPDATE_TIME" IS '更新时间';
;