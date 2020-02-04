package com.css.modules.lawprojectresultt.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import com.css.modules.lawprojectresultt.entity.PubLawProjectResultTEntity;
import com.css.modules.lawprojectresultt.service.LawProjectResultTService;
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
 * 执法结果信息
 *
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
@RestController
@RequestMapping("lawProjectResultT/lawprojectresultt")
@Api(value = "lawProjectResultT/lawprojectresultt", description = "执法结果信息", tags = "执法结果信息")
public class LawProjectResultTController {
    private static final Logger logger = LoggerFactory.getLogger(LawProjectResultTController.class);
    @Autowired
    private LawProjectResultTService lawProjectResultTService;
    @Autowired
    FileUtils fileUtils;
    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "执法结果信息列表", notes = "返回执法结果信息列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawObject", value = "执法对象", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawTypeId", value = "执法类别", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pubStatus", value = "状态", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "resultTimeStart", value = "执法结果开始时间", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "resultTimeEnd", value = "执法结果结束时间", dataType = "date", paramType = "query")
    })
    public R list(@ApiParam(name = "params", value = "状态", hidden = true) @RequestParam Map<String, Object> params) {
        PageUtils page = lawProjectResultTService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/pubList")
    @ApiOperation(value = "互联网端执法结果信息列表", notes = "返回执法结果信息列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawObject", value = "执法对象", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lawTypeId", value = "执法类别", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "resultTimeStart", value = "执法结果开始时间", dataType = "date", paramType = "query"),
            @ApiImplicitParam(name = "resultTimeEnd", value = "执法结果结束时间", dataType = "date", paramType = "query")
    })
    public R pubList(@ApiParam(name = "params", value = "状态", hidden = true) @RequestParam Map<String, Object> params) {
        params.put("pubStatus", "1");
        PageUtils page = lawProjectResultTService.queryPage(params);
        List<LawProjectResultTEntity> lawProjectResultTEntities = (List<LawProjectResultTEntity>)page.getList();
        PubLawProjectResultTEntity pubLawProjectResultTEntity = null;
        List<PubLawProjectResultTEntity> pubLawProjectResultTEntities = new ArrayList<>();
        for(LawProjectResultTEntity lawProjectResultTEntity: lawProjectResultTEntities){
            pubLawProjectResultTEntity = new PubLawProjectResultTEntity();
            BeanUtil.copyProperties(lawProjectResultTEntity, pubLawProjectResultTEntity);
            pubLawProjectResultTEntities.add(pubLawProjectResultTEntity);

        }
        page.setList(pubLawProjectResultTEntities);
        return R.ok().put("page", page);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加执法结果信息", produces = "application/json")
    public R save(@RequestBody LawProjectResultTEntity lawProjectResultT) {
        ValidatorUtils.validateEntity(lawProjectResultT, AddGroup.class);
        lawProjectResultTService.saveLawProjectResultT(lawProjectResultT);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改执法结果信息", produces = "application/json")
    public R update(@RequestBody LawProjectResultTEntity lawProjectResultT) {
        ValidatorUtils.validateEntity(lawProjectResultT, UpdateGroup.class);
        lawProjectResultTService.updateLawProjectResultT(lawProjectResultT);
        return R.ok();
    }

    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        lawProjectResultTService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        lawProjectResultTService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 全部发布
     */
    @GetMapping("/publishAll")
    @ApiOperation(value = "全部发布", produces = "application/json")
    public R publishAll() {
        lawProjectResultTService.commonUpdatePubStatusAll("1");
        return R.ok();
    }

    /**
     * 批量发布
     */
    @PostMapping("/publishBatch")
    @ApiOperation(value = "批量发布", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R publishBatch(@RequestBody Map<String, Object> mapIds) {
        lawProjectResultTService.commonUpdatePubStatusBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 全部撤销
     */
    @GetMapping("/cancelAll")
    @ApiOperation(value = "全部撤销", produces = "application/json")
    public R cancelAll() {
        lawProjectResultTService.commonUpdatePubStatusAll("2");
        return R.ok();
    }

    /**
     * 批量撤销
     */
    @PostMapping("/cancelBatch")
    @ApiOperation(value = "批量撤销", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R cancelBatch(@RequestBody Map<String, Object> mapIds) {
        lawProjectResultTService.commonUpdatePubStatusBatch("2", mapIds);
        return R.ok();
    }

    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        String message = lawProjectResultTService.importLawProjectResultT(file);
        return new R().put("message", message);
    }

    /**
     * 全部导出
     */
    @GetMapping(value = "/downloadAll")
    @ApiOperation(value = "全部导出", produces = "application/octet-stream")
    public void downloadAll(HttpServletResponse response) throws Exception {
        List<LawProjectResultTEntity> list = lawProjectResultTService.downloadAll();
        ExportExcelUtil.exportExcel(list, LawProjectResultTEntity.class, "执法结果信息.xls", response);
    }

    /**
     * 批量导出
     */
    @PostMapping("/downloadBatch")
    @ApiOperation(value = "批量导出", produces = "application/octet-stream")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public void downloadBatch(HttpServletResponse response, @RequestBody Map<String, Object> mapIds) throws Exception {
        List<LawProjectResultTEntity> list = lawProjectResultTService.downloadBatch(mapIds);
        ExportExcelUtil.exportExcel(list, LawProjectResultTEntity.class, "执法结果信息.xls", response);
    }

    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();

        fileUtils.download(path, "执法结果信息模板.xls", response, request);
    }
    /**
     * 按执法类别 执法地域 执法时间 统计信息 和 执法类别统计各年度信息 执法地域统计各年度信息
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "统计信息",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "yearStart", value = "开始年份", dataType = "int", paramType = "query",required = true),
            @ApiImplicitParam(name = "yearEnd", value = "结束年份", dataType = "int", paramType = "query",required = true)
    })
    public R statistics(int yearStart,int yearEnd){
        //数据库所有数据
        List<LawProjectResultTEntity> list = lawProjectResultTService.list(new QueryWrapper<LawProjectResultTEntity>()
                .ne("DEL_FLAG", "1"));

        //将执法结果时间 格式化到年 放入 lawResultTimeStr
        List<LawProjectResultTEntity> processedList = lawProjectResultTService.getProcessedData(list);

        //数据的总数
        long sum = list.size();

        //按执法类别分组的数据,带百分比
        List<JSONObject> jsonObjectLawTypeNameWithProportionList = lawProjectResultTService.getLawTypeNameStatistics(processedList);
        //按执法地域分组的数据,带百分比
        List<JSONObject> jsonObjectProvinceList = lawProjectResultTService.getProvinceStatistics(processedList);

        //按执法结果日期分组的数据，不带百分比
        List<JSONObject> jsonObjectLawResultTimeList = lawProjectResultTService.getLawResultTimeStrStatistics(processedList,yearStart,yearEnd);
        //按执法类别 统计近五年的年度数据
        List<JSONObject> jsonObjectLawTypeNameWithYearsList = lawProjectResultTService.getLawTypeNameWithYearsStatistics(processedList,yearStart,yearEnd);

        // 按执法地域统计各年度信息
        List<JSONObject> jsonObjecProvinceWithYearsList = lawProjectResultTService.getProvinceWithYearsStatistics(processedList,yearStart,yearEnd);

        JSONObject result = new JSONObject();
        result.put("lawTypeNameWithProportion",jsonObjectLawTypeNameWithProportionList);
        result.put("provinceProportion",jsonObjectProvinceList);
        result.put("lawResultTime",jsonObjectLawResultTimeList);
        result.put("lawTypeNameWithYears",jsonObjectLawTypeNameWithYearsList);
        result.put("provinceWithYears",jsonObjecProvinceWithYearsList);

        //将数据格式化json
        Map resultJson = new Gson().fromJson(result.toString(), new TypeToken<Map<Object,List<Map<Object,Object>>>>(){}.getType());

        return R.ok().put("data",resultJson);
    }

}
