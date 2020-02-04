package com.css.modules.exam.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 考试批改表
 * 
 * @author 
 * @email 
 * @date 2019-11-28 16:35:52
 */
@Data
@TableName("LAW_EXAM_CORRECT_T")
public class LawExamCorrectTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	@Excel(name = "序号",orderNum = "1")
	private String id;
	/**
	 * 试卷ID
	 */
	private String testId;
	/**
	 * 试卷名称
	 */
	@Excel(name = "试卷名称",orderNum = "2", isImportField = "true")
	private String testName;
	/**
	 * 考试日期
	 */
	@Excel(name = "考试日期",format = "yyyy-MM-dd",orderNum = "3", isImportField = "true")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date examTime;
	/**
	 * 考试日期
	 */
	@JsonIgnore
	@TableField(exist = false)
	private String examTimeStr;
	/**
	 * 考试开始时间
	 */
	@Excel(name = "考试开始时间",width = 12,format = "HH:mm:ss",orderNum = "4", isImportField = "true")
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	private Date examStartTime;
	/**
	 * 考试结束时间
	 */
	@Excel(name = "考试结束时间",width = 12,format = "HH:mm:ss",orderNum = "5", isImportField = "true")
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	private Date examEndTime;
	/**
	 * 考生ID
	 */
	private String userId;
	/**
	 * 考生姓名
	 */
	@Excel(name = "考生姓名",orderNum = "6", isImportField = "true")
	private String userName;
	/**
	 * 系统批改得分
	 */
	@Excel(name = "系统批改得分",width = 12,orderNum = "7", isImportField = "true")
	private int sysScore;

	/**
	 * 案例解析题名称
	 */
	@Excel(name = "案例解析题名称",width = 15,orderNum = "8", isImportField = "true")
	private String caseAnalysisName;
	/**
	 * 批改状态
	 * 默认 未批改 0 已批改 1
	 */
	@Excel(name = "批改状态",replace = {"未批改_0","已批改_1"},orderNum = "9", isImportField = "true")
	private String status;
	/**
	 * 人工批改ID
	 */
	private String synIds;
	/**
	 * 人工批改得分
	 */
	@Excel(name = "案例解析得分",width = 12,orderNum = "10", isImportField = "true")
	private int synScore;
	/**
	 * 总分数
	 */
	@Excel(name = "总分数",orderNum = "11", isImportField = "true")
	private Integer finalScore;
	/**
	 * 是否合格
	 */
	@Excel(name = "是否合格",replace = {"否_0","是_1"," _null"},orderNum = "12", isImportField = "true")
	private String isQualified;


	/**
	 * 同步信息状态 默认 0 不显示同步信息按钮，1 显示同步信息按钮，2 已经同步信息
	 */
	private String sysncInfoStatus;

}
