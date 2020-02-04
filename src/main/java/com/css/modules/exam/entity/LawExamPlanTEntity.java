package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 考试计划表
 * 
 * @author 
 * @email 
 * @date 2019-11-27 10:13:16
 */
@Data
@TableName("LAW_EXAM_PLAN_T")
public class LawExamPlanTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 试卷ID
	 */
	private String testId;
	/**
	 * 试卷名称
	 */
	private String testName;
	/**
	 * 考试日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date examTime;
	/**
	 * 考试开始时间
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	private Date examStartTime;
	/**
	 * 考试结束时间
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	private Date examEndTime;
	/**
	 * 答题时长
	 */
	private int limitTime;
	/**
	 * 考生名单ID
	 */
	private String listId;
	/**
	 * 考生名单文件名称
	 */
	private String listName;
	/**
	 * 考生名单文件存储名称
	 */
	private String listSaveName;
	/**
	 * 考生名单URL
	 */
	private String listUrl;
	/**
	 * 考生人数\n考生人数
	 */
	private int joinNum;
	/**
	 * 考试计划 状态  0 未开始， 1 考试中,2 已结束，默认为0
	 */
	private String status;
	/**
	 * 发短信通知 状态  0 未发送 ，1 已发送，默认为0
	 */
	private String pushMsgFlag;


}
