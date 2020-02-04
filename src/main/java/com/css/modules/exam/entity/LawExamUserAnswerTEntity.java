package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 考生答案表
 * 
 * @author 
 * @email 
 * @date 2019-11-28 16:35:52
 */
@Data
@TableName("LAW_EXAM_USER_ANSWER_T")
public class LawExamUserAnswerTEntity extends BaseEntity {
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
	 * 题型ID
	 */
	private String questionTypeId;
	/**
	 * 题目ID
	 */
	private String questionId;
	/**
	 * 考试批次ID
	 */
	private String correctId;
	/**
	 * 考生ID
	 */
	private String userId;
	/**
	 * 答案结果
	 */
	private String answerResult;
	/**
	 * 考生答案得分
	 */
	@JsonIgnore
	private int answerScore;

}
