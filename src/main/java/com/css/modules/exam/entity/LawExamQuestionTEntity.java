package com.css.modules.exam.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 题库信息表
 * 
 * @author 
 * @email 
 * @date 2019-11-26 11:42:46
 */
@Data
@TableName("LAW_EXAM_QUESTION_T")
public class LawExamQuestionTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键\n主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 题型ID
	 */
	@NotBlank(message="题型不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String questionTypeId;
	/**
	 * 题型名称
	 */
	@TableField(exist = false)
	@Excel(name = "题型" ,orderNum = "1", isImportField = "true")
	private String questionTypeName;
	/**
	 * 分值(分)
	 */
	@TableField(exist = false)
	private String examScore;
	/**
	 * 题目
	 */
	@Excel(name = "题目" ,orderNum = "2", isImportField = "true")
	@NotBlank(message="题目不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String subject;
	/**
	 * 选项或问题描述
	 */
	@Excel(name = "选项或问题描述" ,orderNum = "3", isImportField = "true")
	@NotBlank(message="选项或问题描述不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String problemDescribe;
	/**
	 * 导入日期
	 */
	private Date importTime;
	/**
	 * 答案
	 */
	@Excel(name = "答案" ,orderNum = "4", isImportField = "true")
	@NotBlank(message="答案不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String answerResult;

	/**
	 * 题目编号
	 */
	@TableField(exist = false)
	private int questionNum;

	/**
	 * 题目顺序号
	 */
	@TableField(exist = false)
	private int orderNum;

}
