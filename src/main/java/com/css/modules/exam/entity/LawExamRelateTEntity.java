package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.beans.beancontext.BeanContextChildSupport;
import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import lombok.Data;

/**
 * 试卷关联题型表
 * 
 * @author 
 * @email 
 * @date 2019-11-26 16:39:24
 */
@Data
@TableName("LAW_EXAM_RELATE_T")
public class LawExamRelateTEntity extends BaseEntity {
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
	 * 题目ID
	 */
	private String questionId;
	/**
	 * 题目编号
	 */
	private int questionNum;
	/**
	 * 顺序
	 */
	private int orderNum;



}
