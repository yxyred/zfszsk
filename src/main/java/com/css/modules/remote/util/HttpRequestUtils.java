package com.css.modules.remote.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.slw.rest.client.util.TokenUtil;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * 使用方式：get请求将参数写入url中，post请求将参数写在requestBody中
 * 远程调用工具
 */
public class HttpRequestUtils {
    private RestTemplate restTemplate;
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * get请求
     */
    public String sendHttpRequestForm(String url, MultiValueMap<String, String> requestBody, HttpMethod httpMethod) {
        //header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("slw.jwt.token", getToken());
        //body
        if(null == requestBody){
            requestBody = new LinkedMultiValueMap<>();
        }
        //httpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);


        ResponseEntity<String> resultResponseEntity = this.restTemplate.exchange(
                url, httpMethod, requestEntity, String.class);
        String body = null;
        if (resultResponseEntity.getStatusCode() == HttpStatus.OK) {
            body = resultResponseEntity.getBody();
        }
        return body;
    }

    /**
     * post请求
     * @param url
     * @param requestBody
     * @param paramsUrl
     * @param httpMethod
     * @return
     */
    public Object sendHttpRequestForm(String url,String requestBody, MultiValueMap<String, String> paramsUrl, HttpMethod httpMethod) {
        //header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestHeaders.add("slw.jwt.token",getToken());
        //body
        if(null == paramsUrl){
            paramsUrl = new LinkedMultiValueMap<>();
        }
        //httpEntity
//        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        HttpEntity requestEntity = new HttpEntity(requestBody, requestHeaders);

//        ResponseEntity<String> resultResponseEntity = null;


        ResponseEntity resultResponseEntity = this.restTemplate.exchange(
                    url, httpMethod, requestEntity, String.class, paramsUrl);
        Object body = null;
        if (resultResponseEntity.getStatusCode() == HttpStatus.OK) {
            body = JSON.parse(resultResponseEntity.getBody().toString());

        } else {
            body = (String)resultResponseEntity.getBody();
        }
        return body;
    }
    //获取token
    private String getToken(){
        return TokenUtil.getToken();

    }





}
