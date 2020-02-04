package com.css.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.css.common.utils.DateUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 *
 * 处理新增和更新的基础数据填充，配合BaseEntity和MyBatisPlusConfig使用
 * @author liukai
 * @date 2019-11-12 17:17:24
 */
@Component
public class MetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        ServletRequest servletRequest = ((WebSubject) SecurityUtils.getSubject()).getServletRequest();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String userId = (String) request.getAttribute("userId");
        Date date = DateUtils.stringToDate(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN), DateUtils.DATE_TIME_PATTERN);
        this.setFieldValByName("createTime", date, metaObject);
        this.setFieldValByName("creatorId", userId, metaObject);
        this.setFieldValByName("updateTime", date, metaObject);
        this.setFieldValByName("updateUserId", userId, metaObject);
        this.setFieldValByName("delFlag", "0", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        ServletRequest servletRequest = ((WebSubject) SecurityUtils.getSubject()).getServletRequest();
        Date date = DateUtils.stringToDate(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN), DateUtils.DATE_TIME_PATTERN);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String userId = (String) request.getAttribute("userId");
        this.setFieldValByName("updateTime", date, metaObject);
        this.setFieldValByName("updateUserId", userId, metaObject);
    }
}
