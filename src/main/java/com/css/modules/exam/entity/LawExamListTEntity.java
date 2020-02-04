package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import lombok.Data;

/**
 * 考生名单关联表
 * 
 * @author 
 * @email 
 * @date 2019-11-27 10:13:16
 */
@Data
@TableName("LAW_EXAM_LIST_T")
public class LawExamListTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键\n主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 考试计划ID\n考试计划ID
	 */
	private String planId;
	/**
	 * 考生ID\n考生ID
	 */
	private String userId;


}
