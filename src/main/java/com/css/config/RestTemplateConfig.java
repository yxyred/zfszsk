package com.css.config;

import com.css.common.exception.CvaException;
import com.css.modules.remote.util.HttpRequestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().set(1,
                new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.setErrorHandler(new ThrowErrorHandler());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(10000);//从连接池获取连接超时
        factory.setReadTimeout(10000);//从服务器读取数据超时  单位ms
        factory.setConnectTimeout(5000);//客户端和服务端建立连接超时
        return factory;
    }

    /**
     * 注入封装RestTemplate
     * @Title: httpRequestUtils
     * @return httpRequestUtils
     */
    @Bean(name = "httpRequestUtils")
    public HttpRequestUtils httpRequestUtils(RestTemplate restTemplate) {
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
        httpRequestUtils.setRestTemplate(restTemplate);
        return httpRequestUtils;
    }
    class ThrowErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            //返回200正常，其他状态码直接抛出500，连接超时。
            if(HttpStatus.OK.value() == response.getStatusCode().value()){
                return false;
            }
            return true;
        }
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            //有错直接抛连接超时500
            throw new CvaException("连接超时"+response.getStatusText(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
