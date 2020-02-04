package com.css.modules.app.interceptor;

import com.css.common.utils.OAuthInterceptorUtil;
import com.css.common.utils.R;
import com.css.common.utils.SsoUrlUtils;
import com.css.modules.app.annotation.Login;
import com.css.modules.app.utils.JwtUtils;
import com.css.modules.sys.service.SysUserTokenService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slw.sso.client.SsoClient;
import org.slw.sso.user.SsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    public static final String USER_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else{
            return true;
        }

        if(annotation == null){
            return true;
        }
        SsoUser ssoUser = SsoClient.getSsoUser(request, response);
        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtils.getHeader());
        }


        /*if(null == ssoUser && null != token){
            Claims claims = jwtUtils.getClaimByToken(token);
            if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
                //token失效返回401，弹到登录页面重新登录
                OAuthInterceptorUtil.returnJson(response, R.error(HttpStatus.SC_UNAUTHORIZED, "invalid token").put("redirectUrl", ssoUrlUtils.getUrl()));
                return false;
            }
            //如果ssouser为空，token不为空并且token未失效的时候直接退出登录，返回给前端退出url
            sysUserTokenService.logout(claims.getSubject());
            OAuthInterceptorUtil.returnJson(response, R.error(HttpStatus.SC_UNAUTHORIZED, "invalid token").put("redirectUrl", ssoUrlUtils.getQuitUrl()));
            return false;
        }*/
        return OAuthInterceptorUtil.oAuthInterceptor(request, response, token,jwtUtils, ssoUrlUtils, ssoUser);
        
    }


}
