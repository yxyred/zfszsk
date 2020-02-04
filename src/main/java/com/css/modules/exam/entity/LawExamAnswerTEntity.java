package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目答案表
 * 
 * @author 
 * @email 
 * @date 2019-11-26 11:12:18
 */
@Data
@TableName("LAW_EXAM_ANSWER_T")
public class LawExamAnswerTEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 关联题目ID\n关联题目ID
	 */
	@TableId
	private String id;
	/**
	 * 答案\n答案
	 */
	private String examResult;
	/**
	 * 创建时间\n系统预留字段
	 */
	private Date createTime;
	/**
	 * 更新时间\n系统预留字段
	 */
	private Date updateTime;
	/**
	 * 创建人ID\n系统预留字段
	 */
	private String creatorId;
	/**
	 * 修改人员ID\n系统预留字段
	 */
	private String updateUserId;
	/**
	 * 删除标志\n禁止硬删除数据
默认0是有效，1是删除

	 */
	private String delFlag;

}
