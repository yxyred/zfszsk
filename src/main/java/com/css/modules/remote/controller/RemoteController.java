package com.css.modules.remote.controller;

import com.css.modules.remote.util.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequestMapping("/remote")
//允许跨域访问
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RemoteController {

    @Autowired
    private HttpRequestUtils httpRequestUtils;

    /**
     * 测试get远程调用
     *
     * @param loginName 登录名
     * @return
     */
    @GetMapping("/find-user/{loginName}")
    public void findTwo(@PathVariable String loginName) throws IOException {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        //String s = httpRequestUtils.sendHttpRequestForm("http://10.13.20.5:6005/microservice/api/suser/listAllUser", requestBody,HttpMethod.GET);
        //System.out.println(s);
    }

    /**
     * 测试post
     * @param loginName
     * @throws IOException
     */
    @GetMapping("/find-user-three/{loginName}")
    public void findThree(@PathVariable String loginName) throws IOException {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        //String s = httpRequestUtils.sendHttpRequestForm("http://10.13.20.5:6005/microservice/api/suser/listAllUser", requestBody,HttpMethod.POST);
        //System.out.println(s);
    }

}
