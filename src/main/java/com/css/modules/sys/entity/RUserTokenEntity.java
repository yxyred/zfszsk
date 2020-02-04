package com.css.modules.sys.entity;

import java.io.Serializable;

public class RUserTokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String loginName;
    private String userName;
    private String token;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
