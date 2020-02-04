package com.css.modules.sys.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.exception.CvaException;
import com.css.common.utils.SsoUrlUtils;
import com.css.modules.remote.util.HttpRequestUtils;
import com.css.modules.sys.entity.RUserEntity;
import com.css.modules.sys.entity.SysUserTokenEntity;
import com.css.modules.sys.service.SysUserTokenService;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 认证
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)，权限通过调用微服务接口实现
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*SysUserTokenEntity userToken = (SysUserTokenEntity)principals.getPrimaryPrincipal();
        String userId = userToken.getUserId();
*/
        //用户权限列表
        //Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用),
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        //在filter中校验了token，这里就不认证了。
        ServletRequest servletRequest = ((WebSubject) SecurityUtils.getSubject()).getServletRequest();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String userId = (String) request.getAttribute("userId");
        //拿到userId后远程调用获取用户信息,如果可以拿到则直接插到自己库里面一份
        String userStr = httpRequestUtils.sendHttpRequestForm( ssoUrlUtils.getGetUserByUserId() + userId, null, HttpMethod.GET);
        //String userStr = httpRequestUtils.sendHttpRequestForm( ssoUrlUtils.getGetUserByUserId() + userId, null, HttpMethod.GET);
        if(null == userStr){
            throw new CvaException("无权限", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        RUserEntity rUserEntity = new Gson().fromJson(userStr, RUserEntity.class);
        SysUserTokenEntity userTokenEntity = new SysUserTokenEntity();
        userTokenEntity.setUserId(userId);
        userTokenEntity.setToken(accessToken);
        if(null != rUserEntity){
            userTokenEntity.setIdCardNo(rUserEntity.getIdcardNo());
        }
        //插入
        List<SysUserTokenEntity> SysUserTokenEntitys = sysUserTokenService.list(new QueryWrapper<SysUserTokenEntity>().eq("user_id", userId));
        if(null == SysUserTokenEntitys || SysUserTokenEntitys.size() ==0){
            sysUserTokenService.save(userTokenEntity);
        }else {
            sysUserTokenService.update(userTokenEntity, new QueryWrapper<>());
        }

        //账号锁定
        /*if(user.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }*/
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userTokenEntity, accessToken,  getName());
        return info;
    }
}
