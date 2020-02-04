package com.css.modules.message.dao;

import com.css.modules.message.entity.MessageCenterTEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息中心表
 * 
 * @author liukai
 * @date 2019-12-13 15:14:50
 */
@Mapper
public interface MessageCenterTDao extends BaseMapper<MessageCenterTEntity> {
	
}
