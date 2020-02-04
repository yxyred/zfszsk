package com.css.modules.exam.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 考生信息表
 * 
 * @author 
 * @email 
 * @date 2019-11-27 10:13:16
 */
@Data
@TableName("LAW_EXAM_USER_T")
public class LawExamUserTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	@Excel(name = "序号",orderNum = "1")
	private String id;

	/**
	 * 考生姓名
	 */
	@Excel(name = "考生姓名",orderNum = "2", isImportField = "true")
	@NotBlank(message="考生姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 200 ,message = "考生姓名最大长度为200", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 身份证号码
	 */
	@Excel(name = "身份证号码",width = 20,orderNum = "3", isImportField = "true")
	@NotBlank(message="身份证号码不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 18 ,message = "身份证号码最大长度为18", groups = {AddGroup.class, UpdateGroup.class})
	private String idcard;
	/**
	 * 性别
	 */
	@Excel(name = "性别",replace = {"男_1","女_2"," _null"},orderNum = "4", isImportField = "true")
	@NotBlank(message="性别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 3 ,message = "性别最大长度为3", groups = {AddGroup.class, UpdateGroup.class})
	private String sex;
	/**
	 * 所在单位
	 */
	@Excel(name = "所在单位",width = 30,orderNum = "5", isImportField = "true")
	@NotBlank(message="所在单位不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 200 ,message = "所在单位最大长度为200", groups = {AddGroup.class, UpdateGroup.class})
	private String placeInUnit;
	/**
	 * 联系方式
	 */
	@Excel(name = "联系方式",width = 15,orderNum = "6", isImportField = "true")
	@NotBlank(message="联系方式不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 11 ,message = "联系方式最大长度为11", groups = {AddGroup.class, UpdateGroup.class})
	private String contactInfo;
	/**
	 * 考试计划ID
	 */
	private String planId;
	/**
	 * 试卷ID
	 */
	private String testId;
	/**
	 * 试卷名称
	 */
	@Excel(name = "试卷名称",width = 25,orderNum = "7")
	private String testName;

	/**
	 * 考试日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	@Excel(name = "考试日期",format = "yyyy-MM-dd",orderNum = "8")
	private Date examTime;
	/**
	 * 考试开始时间
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	@Excel(name = "考试开始时间",width = 12,format = "HH:mm:ss",orderNum = "9")
	private Date examStartTime;
	/**
	 * 考试结束时间
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	@Excel(name = "考试结束时间",width = 12,format = "HH:mm:ss",orderNum = "10")
	private Date examEndTime;
	/**
	 * 考试分数
	 */
	@Excel(name = "考试分数",orderNum = "11")
	private int examScore;
	/**
	 * 是否合格
	 */
	@Excel(name = "是否合格",replace = {"合格_1","不合格_2"," _null"},orderNum = "12")
	private String isQualified;
	/**
	 * 是否已读
	 */
	private String isRead;
}
