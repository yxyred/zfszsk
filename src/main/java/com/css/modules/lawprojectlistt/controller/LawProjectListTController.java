package com.css.modules.lawprojectlistt.controller;

import java.io.File;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import com.css.modules.lawprojectlistt.entity.PubLawProjectListTEntity;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.lawprojectlistt.service.LawProjectListTService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 执法事项清单
 *
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
@RestController
@RequestMapping("lawProjectListT/lawprojectlistt")
@Api(value = "lawProjectListT/lawprojectlistt", description = "执法事项清单", tags = "执法事项清单")
public class LawProjectListTController {
    private static final Logger logger = LoggerFactory.getLogger(LawProjectListTController.class);
    @Autowired
    private LawProjectListTService lawProjectListTService;
    @Autowired
    FileUtils fileUtils;
    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "执法事项清单列表", notes = "返回执法事项清单列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawMain", value = "执法主体", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawTypeId", value = "执法类别", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pubStatus", value = "状态", dataType = "string", paramType = "query")
    })
    public R list(@ApiParam(name = "params", hidden = true) @RequestParam Map<String, Object> params) {
        PageUtils page = lawProjectListTService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @GetMapping("/pubList")
    @ApiOperation(value = "执法事项清单列表", notes = "返回执法事项清单列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawMain", value = "执法主体", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawTypeId", value = "执法类别", dataType = "string", paramType = "query"),

    })
    public R pubList(@ApiParam(name = "params", hidden = true) @RequestParam Map<String, Object> params) {
        params.put("pubStatus", "1");
        PageUtils page = lawProjectListTService.queryPage(params);
        List<LawProjectListTEntity> lawProjectListTEntities = (List<LawProjectListTEntity>)page.getList();
        PubLawProjectListTEntity pubLawProjectListTEntity = null;
        List<PubLawProjectListTEntity> pubLawProjectListTEntities = new ArrayList<>();
        for(LawProjectListTEntity lawProjectListTEntity: lawProjectListTEntities){
            pubLawProjectListTEntity = new PubLawProjectListTEntity();
            BeanUtil.copyProperties(lawProjectListTEntity, pubLawProjectListTEntity);
            pubLawProjectListTEntities.add(pubLawProjectListTEntity);
        }
        page.setList(pubLawProjectListTEntities);
        return R.ok().put("page", page);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加执法事项清单", produces = "application/json")
    public R save(@RequestBody LawProjectListTEntity lawProjectListT) {
        ValidatorUtils.validateEntity(lawProjectListT, AddGroup.class);
        lawProjectListTService.saveLawProjectListT(lawProjectListT);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改执法事项清单", produces = "application/json")
    public R update(@RequestBody LawProjectListTEntity lawProjectListT) {
        ValidatorUtils.validateEntity(lawProjectListT, UpdateGroup.class);
        lawProjectListTService.updateLawProjectListT(lawProjectListT);
        return R.ok();
    }

    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        lawProjectListTService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        lawProjectListTService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 全部发布
     */
    @GetMapping("/publishAll")
    @ApiOperation(value = "全部发布", produces = "application/json")
    public R publishAll() {
        lawProjectListTService.commonUpdatePubStatusAll("1");
        return R.ok();
    }

    /**
     * 批量发布
     */
    @PostMapping("/publishBatch")
    @ApiOperation(value = "批量发布", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R publishBatch(@RequestBody Map<String, Object> mapIds) {
        lawProjectListTService.commonUpdatePubStatusBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 全部撤销
     */
    @GetMapping("/cancelAll")
    @ApiOperation(value = "全部撤销", produces = "application/json")
    public R cancelAll() {
        lawProjectListTService.commonUpdatePubStatusAll("2");
        return R.ok();
    }

    /**
     * 批量撤销
     */
    @PostMapping("/cancelBatch")
    @ApiOperation(value = "批量撤销", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R cancelBatch(@RequestBody Map<String, Object> mapIds) {
        lawProjectListTService.commonUpdatePubStatusBatch("2", mapIds);
        return R.ok();
    }

    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        String message = lawProjectListTService.importLawProjectListT(file);
        return new R().put("message", message);
    }

    /**
     * 全部导出
     */
    @GetMapping(value = "/downloadAll")
    @ApiOperation(value = "全部导出", produces = "application/octet-stream")
    public void downloadAll(HttpServletResponse response) throws Exception {
        List<LawProjectListTEntity> list = lawProjectListTService.downloadAll();
        ExportExcelUtil.exportExcel(list, LawProjectListTEntity.class, "执法事项清单.xls", response);
    }

    /**
     * 批量导出
     */
    @PostMapping("/downloadBatch")
    @ApiOperation(value = "批量导出", produces = "application/octet-stream")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public void downloadBatch(HttpServletResponse response, @RequestBody Map<String, Object> mapIds) throws Exception {
        List<LawProjectListTEntity> list = lawProjectListTService.downloadBatch(mapIds);
        ExportExcelUtil.exportExcel(list, LawProjectListTEntity.class, "执法事项清单.xls", response);
    }

    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();

        fileUtils.download(path, "执法事项清单模板.xls", response, request);
    }

    /**
     * 按执法类别 执法事项 统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "统计信息",
            produces = "application/json")

    public R statistics(){
        //数据库所有数据
        List<LawProjectListTEntity> list = lawProjectListTService.list(new QueryWrapper<LawProjectListTEntity>()
                .ne("DEL_FLAG", "1"));

        List<LawProjectListTEntity> processedList = lawProjectListTService.getProcessedData(list);
        //按执法主体分组的数据
        List<JSONObject> jsonObjectLawMainList = lawProjectListTService.getLawMainListStatistics(processedList);
        //按执法类别分组的数据
        List<JSONObject> jsonObjectLawTypeNameList = lawProjectListTService.getLawTypeNameStatistics(processedList);

        JSONObject result = new JSONObject();
        result.put("lawMain",jsonObjectLawMainList);
        result.put("lawTypeName",jsonObjectLawTypeNameList);

        //将数据格式化json
        Map resultJson = new Gson().fromJson(result.toString(), new TypeToken<Map<String,List<Map<String,String>>>>(){}.getType());
        return R.ok().put("data",resultJson);

    }

}
