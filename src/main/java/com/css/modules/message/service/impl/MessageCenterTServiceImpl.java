package com.css.modules.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;
import com.css.common.utils.SsoUrlUtils;
import com.css.modules.lawsubjectt.entity.RemoteSubjectEntity;
import com.css.modules.message.dao.MessageCenterTDao;
import com.css.modules.message.entity.MessageCenterTEntity;
import com.css.modules.message.service.MessageCenterTService;
import com.css.modules.remote.util.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("messageCenterTService")
public class MessageCenterTServiceImpl extends ServiceImpl<MessageCenterTDao, MessageCenterTEntity> implements MessageCenterTService {
    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MessageCenterTEntity> page = this.page(
                new Query<MessageCenterTEntity>().getPage(params),
                new QueryWrapper<MessageCenterTEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean pushMessage(MessageCenterTEntity messageCenterTEntity) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sxName", messageCenterTEntity.getMessage());
        paramMap.put("uuid", messageCenterTEntity.getReceiverId());
        paramMap.put("xtId", ssoUrlUtils.getSysId());
        paramMap.put("xtName", ssoUrlUtils.getSysName());
        String paramStr = JSON.toJSONString(paramMap);
        com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject)httpRequestUtils.sendHttpRequestForm(ssoUrlUtils.getSendMessageCenterUrl(), paramStr, null, HttpMethod.POST);
        if(null == jsonObject){
            return false;
        }
        return (Boolean) jsonObject.get("success");
    }

}