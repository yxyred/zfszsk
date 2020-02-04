package com.css.modules.sys.controller;

import com.css.common.utils.R;
import com.css.common.utils.ShiroUtils;
import com.css.modules.sys.service.RFuncService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/func")
@Api("功能接口")
public class RFuncController extends AbstractController {
	@Autowired
	private RFuncService rFuncService;

	/**
	 * 功能列表
	 */
	@ApiOperation("根据用户id获取功能列表")
	@GetMapping("/list")
	public R nav(){
		String funcStr = rFuncService.getFuncByUserId(ShiroUtils.getUserId());
        List funcList = new Gson().fromJson(funcStr, List.class);
		return R.ok().put("funcList", funcList);
	}




}
