package com.css.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.modules.sys.entity.RRoleEntity;
import com.css.modules.sys.entity.SysRoleEntity;

import java.util.List;


/**
 * 角色
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    List<RRoleEntity> getRoleByUserId();

}
