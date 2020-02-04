package com.css.modules.exam.entity;


import com.css.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 考试计划表
 * 
 * @author 
 * @email 
 * @date 2019-11-27 10:13:16
 */
@Data
public class NetLawExamPlanTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
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
	 * 考试计划 状态  0 未开始， 1 考试中,2 已结束，默认为0
	 */
	private String status;


}
