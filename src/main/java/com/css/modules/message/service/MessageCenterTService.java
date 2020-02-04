package com.css.modules.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.message.entity.MessageCenterTEntity;

import java.util.Map;

/**
 * 消息中心表
 *
 * @author liukai
 * @date 2019-12-13 15:14:50
 */
public interface MessageCenterTService extends IService<MessageCenterTEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 推送消息，成功返回true
     * @param messageCenterTEntity
     * @return
     */
    boolean pushMessage(MessageCenterTEntity messageCenterTEntity);
}

