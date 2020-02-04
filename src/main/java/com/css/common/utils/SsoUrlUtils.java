package com.css.common.utils;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sso")
@Component
@Data
public class SsoUrlUtils {
    private String url;

    private String getUserByUserId;

    private String getMenu;

    private String funcUrl;

    private String dictListByTableUrl;

    private String dictsByDictCodeUrl;

    private  String rl;

    private String cl;

    private String quitUrl;

    private String queryCompany;

    private String pushMessageUrl;

    private String messageTypeUrl;

    private String sendMessageCenterUrl;

    private String sysId;

    private String sysName;

    private String uploadDfsBackEndUrl;

    private String downloadUrl;

    private String roleByUserId;



}
