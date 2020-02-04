package com.css.modules.sys.controller;

import com.css.modules.sys.entity.SysUserEntity;
import com.css.modules.sys.entity.SysUserTokenEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller公共组件
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SysUserTokenEntity getUser() {
		return (SysUserTokenEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected String getUserId() {
		return getUser().getUserId();
	}
}
