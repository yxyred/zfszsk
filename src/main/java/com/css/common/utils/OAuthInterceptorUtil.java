package com.css.common.utils;

import com.css.modules.app.utils.JwtUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slw.sso.client.SsoClient;
import org.slw.sso.user.SsoUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class OAuthInterceptorUtil {

    public static final String USER_KEY = "userId";


    /**
     * 拦截器逻辑,用于不适用shiro时
     * @param request
     * @param response
     * @param jwtUtils
     * @param token
     * @param ssoUrlUtils
     * @return
     */
    public static boolean oAuthInterceptor(HttpServletRequest request, HttpServletResponse response, String token, JwtUtils jwtUtils, SsoUrlUtils ssoUrlUtils, SsoUser ssoUser) {

        //凭证为空
        if (StringUtils.isBlank(token)) {
            //throw new CvaException(jwtUtils.getHeader() + "不能为空", HttpStatus.UNAUTHORIZED.value());
            if (null == ssoUser) {
                returnJson(response, R.ok().put("redirectUrl", ssoUrlUtils.getUrl()));
                return false;
            } else {
                //生成token
                token = jwtUtils.generateToken(ssoUser.getUserId());
                //要保存到数据库
                //insertOrUpdateToken(ssoUser.getUserId(), token);
                returnJson(response, R.ok().put("token", token));
                return false;
            }
        } else {
            //这里的token不会自动更新超时时间，就这样吧

            Claims claims = jwtUtils.getClaimByToken(token);

            if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
                //token失效返回401，弹到登录页面重新登录
                returnJson(response, R.error(HttpStatus.SC_UNAUTHORIZED, "invalid token").put("redirectUrl", ssoUrlUtils.getQuitUrl()));
                return false;
            }
            //设置userId到request里，后续根据userId，获取用户信息。这里有个类型错误
            request.setAttribute(USER_KEY, claims.getSubject());
            //只有token验证通过可以通过拦截器，其他情况都false
            return true;
        }
    }

    /**
     * json返回
     *
     * @param response
     * @param result
     */
    public static void returnJson(HttpServletResponse response, R result) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            String json = new Gson().toJson(result);
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }

        }
    }




}
