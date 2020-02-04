

package com.css.modules.app.controller;


import com.css.common.utils.R;
import com.css.modules.app.annotation.Login;
import com.css.modules.app.annotation.LoginUser;
import com.css.modules.app.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * APP测试接口
 */
@RestController
@RequestMapping("/app")
@Api("APP测试接口")
public class AppTestController {
    @Login
    @GetMapping("userInfo")
    @ApiOperation("获取用户信息")
    public R userInfo(@LoginUser UserEntity user){
        return R.ok().put("user", user);
    }
    @Login
    @GetMapping("userId")
    @ApiOperation("获取用户ID")
    public R userInfo(@RequestAttribute("userId") Integer userId){
        return R.ok().put("userId", userId);
    }

    @GetMapping("notToken")
    @ApiOperation("忽略Token验证测试")
    public R notToken(){
        return R.ok().put("msg", "无需token也能访问。。。");
    }

    @GetMapping("/find-user-id/{loginName}")
    public R findOne(@PathVariable String loginName, HttpServletRequest request, HttpServletResponse response) throws IOException {

        /*SsoUser ssoUser = SsoClient.getSsoUser(request, response);
        if(null == ssoUser){
            response.sendRedirect("http://10.13.20.5:6003/baseSSOzwww?toUrl=http://localhost:8080/csscva/remote/find-user-id/fds");
        }*/


        System.out.println("1212");

        //单点success
        return R.ok().put("1", "1");

    }

}
