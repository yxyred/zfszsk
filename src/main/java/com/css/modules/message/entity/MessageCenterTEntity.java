package com.css.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import lombok.Data;

/**
 * 消息中心表
 * 
 * @author liukai
 * @date 2019-12-13 15:14:50
 */
@Data
@TableName("MESSAGE_CENTER_T")
public class MessageCenterTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 消息
	 */
	private String message;
	/**
	 * 发送人ID
	 */
	private String senderId;
	/**
	 * 接收人ID
	 */
	private String receiverId;

}
