package com.css.common.utils;

import com.alibaba.fastjson.JSON;
import com.css.modules.remote.util.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PushMessageUtils {
    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;

    public  String pushMessage(String appCode){
        String url = ssoUrlUtils.getMessageTypeUrl() + appCode;
        return httpRequestUtils.sendHttpRequestForm(url, null, HttpMethod.GET);
    }

    public  Object pushMessage(){
        String url = ssoUrlUtils.getPushMessageUrl() ;

        Map paramMap = new HashMap();
        List phonelist = new ArrayList();
        phonelist.add("15712345678");
        phonelist.add("15655555555");
        paramMap.put("addrlist",phonelist);
        paramMap.put("messageType","2");
        paramMap.put("messagebody","messa22ge 111cont22ent@@http://www.css.com.cn");
        paramMap.put("sender","执法监督");
        paramMap.put("senderAppCode","1234");
        paramMap.put("title","wan3gpw15f3's 11mw11e4ss33age10");
        String jsonString = JSON.toJSONString(paramMap);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        Object s = httpRequestUtils.sendHttpRequestForm(url, jsonString,map, HttpMethod.POST);
        return s;
    }

    /**
     * 发短信
     * @param phonelist 电话列表
     * @param messagebody 短信内容
     * @param title 短信标题
     * @return
     */
    public  Object pushMessage(List phonelist,String messagebody,String title){
        String url = ssoUrlUtils.getPushMessageUrl() ;
        Map paramMap = new HashMap();
        paramMap.put("addrlist",phonelist);
        paramMap.put("messageType","2");
        paramMap.put("messagebody",messagebody);
        paramMap.put("sender","执法监督");
        paramMap.put("senderAppCode","1234");
        paramMap.put("title",title);
        String jsonString = JSON.toJSONString(paramMap);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        Object s = httpRequestUtils.sendHttpRequestForm(url, jsonString,map, HttpMethod.POST);
        return s;
    }

}
