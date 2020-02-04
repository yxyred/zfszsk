package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import lombok.Data;

/**
 * 考试须知
 * 
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@Data
@TableName("LAW_EXAM_NOTICE_T")
public class LawExamNoticeTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键\n主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 内容\n含具体内容
	 */
	private String content;
	/**
	 * 是否阅读注意事项\n是否阅读注意事项
	 */
	private String isRead;
	/**
	 * 阅读人ID\n阅读人ID
	 */
	private String readUserId;

}
