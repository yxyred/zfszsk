

package com.css.common.utils;

import com.css.modules.sys.entity.SysUserEntity;
import com.css.common.exception.CvaException;
import com.css.modules.sys.entity.SysUserTokenEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slw.rest.client.util.TokenUtil;

/**
 * Shiro工具类
 */
public class ShiroUtils {

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static SysUserTokenEntity getUserEntity() {
		return (SysUserTokenEntity)SecurityUtils.getSubject().getPrincipal();
	}

	public static String getUserId() {
		return getUserEntity().getUserId();
	}
	
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static String getKaptcha(String key) {
		Object kaptcha = getSessionAttribute(key);
		if(kaptcha == null){
			throw new CvaException("验证码已失效");
		}
		getSession().removeAttribute(key);
		return kaptcha.toString();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(TokenUtil.getToken());
		}
	}




}
