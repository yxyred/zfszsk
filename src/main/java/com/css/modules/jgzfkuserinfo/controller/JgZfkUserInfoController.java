package com.css.modules.jgzfkuserinfo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.css.modules.jgzfkuserinfo.entity.JgZfkUserInfoEntity;
import com.css.modules.jgzfkuserinfo.entity.PubJgZfkUserInfoEntity;
import com.css.modules.jgzfkuserinfo.service.JgZfkUserInfoService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 执法检查人员库
 *
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
@RestController
@RequestMapping("jgZfkUserInfo/jgzfkuserinfo")
@Api(value = "jgZfkUserInfo/jgzfkuserinfo", description = "执法检查人员库", tags = "执法检查人员库")
public class JgZfkUserInfoController {
    private static final Logger logger = LoggerFactory.getLogger(JgZfkUserInfoController.class);
    @Autowired
    private JgZfkUserInfoService jgZfkUserInfoService;

    @Autowired
    FileUtils fileUtils;
    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "执法检查人员库列表", notes = "返回执法检查人员库列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "agentname", value = "姓名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "执法证号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pubStatus", value = "状态", dataType = "string", paramType = "query")
    })
    public R list(@ApiParam(name = "params", value = "状态", hidden = true) @RequestParam Map<String, Object> params) {
        PageUtils page = jgZfkUserInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @GetMapping("/pubList")
    @ApiOperation(value = "互联网端执法检查人员库列表", notes = "返回执法检查人员库列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "agentname", value = "姓名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "执法证号", dataType = "string", paramType = "query"),

    })
    public R pubList(@ApiParam(name = "params", value = "状态", hidden = true) @RequestParam Map<String, Object> params) {
        params.put("pubStatus", "1");
        PageUtils page = jgZfkUserInfoService.queryPage(params);
        List<JgZfkUserInfoEntity> JgZfkUserInfoEntitys = (List<JgZfkUserInfoEntity>)page.getList();
        PubJgZfkUserInfoEntity pubJgZfkUserInfoEntity = null;
        List<PubJgZfkUserInfoEntity> pubJgZfkUserInfoEntities = new ArrayList<>();
        for(JgZfkUserInfoEntity jgZfkUserInfoEntity: JgZfkUserInfoEntitys){
            pubJgZfkUserInfoEntity = new PubJgZfkUserInfoEntity();
            BeanUtil.copyProperties(jgZfkUserInfoEntity, pubJgZfkUserInfoEntity);
            pubJgZfkUserInfoEntities.add(pubJgZfkUserInfoEntity);
        }
        page.setList(pubJgZfkUserInfoEntities);
        return R.ok().put("page", page);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加执法检查人员库", produces = "application/json")
    public R save(@RequestBody JgZfkUserInfoEntity jgZfkUserInfo) {
        ValidatorUtils.validateEntity(jgZfkUserInfo, AddGroup.class);
        jgZfkUserInfoService.saveJgZfkUserInfo(jgZfkUserInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改执法检查人员库", produces = "application/json")
    public R update(@RequestBody JgZfkUserInfoEntity jgZfkUserInfo) {
        ValidatorUtils.validateEntity(jgZfkUserInfo, UpdateGroup.class);
        jgZfkUserInfoService.updateJgZfkUserInfo(jgZfkUserInfo);
        return R.ok();
    }

    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        jgZfkUserInfoService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        jgZfkUserInfoService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 全部发布
     */
    @GetMapping("/publishAll")
    @ApiOperation(value = "全部发布", produces = "application/json")
    public R publishAll() {
        jgZfkUserInfoService.commonUpdatePubStatusAll("1");
        return R.ok();
    }

    /**
     * 批量发布
     */
    @PostMapping("/publishBatch")
    @ApiOperation(value = "批量发布", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R publishBatch(@RequestBody Map<String, Object> mapIds) {
        jgZfkUserInfoService.commonUpdatePubStatusBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 全部撤销
     */
    @GetMapping("/cancelAll")
    @ApiOperation(value = "全部撤销", produces = "application/json")
    public R cancelAll() {
        jgZfkUserInfoService.commonUpdatePubStatusAll("2");
        return R.ok();
    }

    /**
     * 批量撤销
     */
    @PostMapping("/cancelBatch")
    @ApiOperation(value = "批量撤销", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R cancelBatch(@RequestBody Map<String, Object> mapIds) {
        jgZfkUserInfoService.commonUpdatePubStatusBatch("2", mapIds);
        return R.ok();
    }

    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        String message = jgZfkUserInfoService.importJgZfkUserInfo(file);
        return new R().put("message", message);
    }

    /**
     * 全部导出
     */
    @GetMapping(value = "/downloadAll")
    @ApiOperation(value = "全部导出", produces = "application/octet-stream")
    public void downloadAll(HttpServletResponse response) throws Exception {
        List<JgZfkUserInfoEntity> list = jgZfkUserInfoService.downloadAll();
        ExportExcelUtil.exportExcel(list, JgZfkUserInfoEntity.class, "执法检查人员库.xls", response);
    }

    /**
     * 批量导出
     */
    @PostMapping("/downloadBatch")
    @ApiOperation(value = "批量导出", produces = "application/octet-stream")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public void downloadBatch(HttpServletResponse response, @RequestBody Map<String, Object> mapIds) throws Exception {
        List<JgZfkUserInfoEntity> list = jgZfkUserInfoService.downloadBatch(mapIds);
        ExportExcelUtil.exportExcel(list, JgZfkUserInfoEntity.class, "执法检查人员库.xls", response);
    }

    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();

        fileUtils.download(path, "执法检查人员库模板.xls", response, request);
    }

    /**
     * 按执法类别 执法事项 统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "统计信息",
            produces = "application/json")
    public R statistics(){
        //数据库所有数据
        List<JgZfkUserInfoEntity> list = jgZfkUserInfoService.list(new QueryWrapper<JgZfkUserInfoEntity>()
                .ne("DEL_FLAG", "1"));

        List<JgZfkUserInfoEntity> processedList = jgZfkUserInfoService.getProcessedData(list);

        //按执法人员所属省份统计
        List<JSONObject> jsonObjectProvinceList = jgZfkUserInfoService.getProvinceStatistics(processedList);

        //按执法年龄段分布
        List<JSONObject> jsonObjectLawTypeNameList = jgZfkUserInfoService.getAgeGroupStatistics(processedList);
        //按执法证件的有效性统计
        List<JSONObject> jsonObjectEffectiveList = jgZfkUserInfoService.getIsEffectiveStatistics(processedList);

        JSONObject result = new JSONObject();
        result.put("lawTypeName",jsonObjectLawTypeNameList);
        result.put("isEffective",jsonObjectEffectiveList);
        result.put("province",jsonObjectProvinceList);
        //将数据格式化json
        Map resultJson = new Gson().fromJson(result.toString(), new TypeToken<Map<String,List<Map<String,String>>>>(){}.getType());
        return R.ok().put("data",resultJson);

    }

    /**
     * 根据省份 获取直返人员全部列表
     */
    @GetMapping("/listByProvince")
    @ApiOperation(value = "根据省份 获取直返人员全部列表", notes = "返回执法检查人员库列表",
            produces = "application/json")
    public R listByProvince(@ApiParam(name = "province", value = "省份", required = true) @RequestParam String province) {
        List<JgZfkUserInfoEntity> list = jgZfkUserInfoService.queryByProvince(province);
        return R.ok().put("data",list);
    }
}
