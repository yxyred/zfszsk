package com.css.modules.sys.controller;

import com.css.common.utils.ShiroUtils;
import com.css.modules.sys.entity.RMenuEntity;
import com.css.modules.sys.entity.RUserEntity;
import com.css.modules.sys.entity.SysMenuEntity;
import com.css.modules.sys.service.ShiroService;
import com.css.modules.sys.service.SysMenuService;
import com.css.common.annotation.SysLog;
import com.css.common.exception.CvaException;
import com.css.common.utils.Constant;
import com.css.common.utils.R;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单
 */
@RestController
@RequestMapping("/sys/menu")
@Api("菜单接口")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 导航菜单"dc6584111008414dbc7bc91475a38508"
	 */
    @ApiOperation("导航菜单")
	@GetMapping("/nav")
	public R nav(){
        String menusStr = sysMenuService.getMenuByUserIdAndSysId(ShiroUtils.getUserId(), "1055");
        List menus = new Gson().fromJson(menusStr, List.class);
        return R.ok().put("menuList", menus);
	}


}
