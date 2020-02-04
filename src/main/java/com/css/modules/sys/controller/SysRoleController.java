package com.css.modules.sys.controller;

import com.css.modules.sys.entity.SysRoleEntity;
import com.css.modules.sys.service.SysRoleMenuService;
import com.css.modules.sys.service.SysRoleService;
import com.css.common.annotation.SysLog;
import com.css.common.utils.Constant;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import com.css.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;


    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    public R list(){
        sysRoleService.getRoleByUserId();
        return null;
    }

}
