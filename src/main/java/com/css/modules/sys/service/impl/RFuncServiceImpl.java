package com.css.modules.sys.service.impl;

import com.css.common.utils.SsoUrlUtils;
import com.css.modules.remote.util.HttpRequestUtils;
import com.css.modules.sys.service.RFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service("rFuncService")
public class RFuncServiceImpl implements RFuncService {

    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;
    @Override
    public String getFuncByUserId(String userId) {
        String url = ssoUrlUtils.getFuncUrl() + "?userId=" + userId;
        return httpRequestUtils.sendHttpRequestForm(url, null, HttpMethod.GET);
    }
}
