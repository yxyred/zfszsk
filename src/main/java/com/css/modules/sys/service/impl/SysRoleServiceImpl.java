package com.css.modules.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.ShiroUtils;
import com.css.common.utils.SsoUrlUtils;
import com.css.modules.remote.util.HttpRequestUtils;
import com.css.modules.sys.dao.SysRoleDao;
import com.css.modules.sys.entity.RRoleEntity;
import com.css.modules.sys.entity.SysRoleEntity;
import com.css.modules.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {


    @Autowired
    private SsoUrlUtils ssoUrlUtils;

    @Autowired
    private HttpRequestUtils httpRequestUtils;

    /**
     * 通过userid获取角色列表
     */
    @Override
    public List<RRoleEntity> getRoleByUserId() {
        String userId = ShiroUtils.getUserId();
        String roleStr = httpRequestUtils.sendHttpRequestForm(ssoUrlUtils.getRoleByUserId() + userId, null, HttpMethod.GET);
        return JSONObject.parseArray(roleStr, RRoleEntity.class);
    }

}
