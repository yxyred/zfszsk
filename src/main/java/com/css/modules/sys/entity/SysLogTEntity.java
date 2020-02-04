package com.css.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.css.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统日志
 */
@Data
@TableName("sys_log_T")
public class SysLogTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.UUID)
	private Long id;
	//用户名
	private String userId;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//操作时间
	private Date operationTime;

}
