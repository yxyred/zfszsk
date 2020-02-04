package com.css.modules.remote.interceptor;

import com.css.common.annotation.SysLog;
import com.css.common.exception.CvaException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;


@Component
public class HttpLoggingInterceptor implements ClientHttpRequestInterceptor {

    @SysLog("远程调用")
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
        } catch (SocketTimeoutException e) {
            throw new CvaException("数据请求超时", HttpStatus.UNAUTHORIZED.value());
        }
        return response;
    }


}
