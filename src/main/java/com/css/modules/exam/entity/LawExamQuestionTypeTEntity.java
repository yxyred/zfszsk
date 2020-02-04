package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import lombok.Data;

/**
 * 试卷关联题目表
 * 
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@Data
@TableName("LAW_EXAM_QUESTION_TYPE_T")
public class LawExamQuestionTypeTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键\n主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 题型名称\n题型名称
	 */
	private String typeName;
	/**
	 * 分值(分)\n分值(分)
	 */
	private String examScore;



}
