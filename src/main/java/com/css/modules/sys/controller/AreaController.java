package com.css.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.enums.DicCodePool;
import com.css.common.enums.LawEnforcementOfficersEnum;
import com.css.common.enums.REGEnum;
import com.css.common.utils.*;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import com.css.modules.remote.util.HttpRequestUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/area")
public class AreaController {
    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private REnumUtil rEnumUtil;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;
    @Autowired
    private AreaUtils areaUtils;



    @GetMapping("/getAllProvince")
    @ApiOperation(value = "查看全国所有的省",
            produces = "application/json")

    //  @ApiOperation("根据名称查询行政区划")
    private R getProvince() {
        List list = areaUtils.getProvince();
        return R.ok().put("list1", list);
    }

    @GetMapping("/getCity")
    @ApiOperation(value = "查看谋省的全部市",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "省的id", dataType = "string", paramType = "query",required = true),
    })
    //  @ApiOperation("根据名称查询行政区划")
    public R getCity(String parentId) {
        String cl = ssoUrlUtils.getCl();

        String dl = cl+parentId;
        String list = httpRequestUtils.sendHttpRequestForm(dl, null, HttpMethod.GET);
        List<Object> list2 = new ArrayList<>();
        String str = StringUtils.substringAfter(list, "data\":");
//        String str2 = str.substring(str.length(),str.length()-1);
        String str2 = str.substring(0, str.length() - 1);

        if (StringUtils.isNotBlank(list)) {
            List listTemp = new Gson().fromJson(str2, List.class);
            list2 = ProcessedData.getProcessedDate(listTemp,  "administrativeAreaName");
        }
        return R.ok().put("list1", list2);
    }

}
