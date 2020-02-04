package com.css.modules.lawsubjectt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.FileUtils;
import com.css.common.utils.ExportExcelUtil;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.css.modules.lawsubjectt.entity.LawSubjectTEntity;
import com.css.modules.lawsubjectt.service.LawSubjectTService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * 市场主体名录表
 *
 * @author
 * @email
 * @date 2019-11-18 14:26:25
 */
@RestController
@Api(tags = "lawsubjectt" ,description = "市场主体名录库")
@RequestMapping("/lawsubjectt")
public class LawSubjectTController {
    @Autowired
    private LawSubjectTService lawSubjectTService;
    @Autowired
    FileUtils fileUtils;
    /**
     * 列表
     */
    @ApiOperation("查询")
    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "uniscid", value = "市场主体统一社会信用代码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "subjectName", value = "市场主体名称", dataType = "string", paramType = "query")

    })
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lawSubjectTService.queryPage(params);

        return R.ok().put("page", page);
    }

    @ApiOperation("通过市场主体统一信用代码获取市场主体信息")
    @GetMapping(value = "/remoteInfo/{uniscid}")
    public R remoteInfo(@PathVariable("uniscid") String uniscid){
        LawSubjectTEntity lawSubjectTEntity = lawSubjectTService.getRemoteSubjectByUniscid(uniscid);
        return R.ok().put("lawSubjectTEntity", lawSubjectTEntity);
    }


    /**
     * 信息
     */
    @ApiOperation("getbyid")
    @GetMapping(value = "/info/{id}")
    public R info(@PathVariable("id") String id){
		LawSubjectTEntity lawSubjectT = lawSubjectTService.getById(id);

        return R.ok().put("lawSubjectT", lawSubjectT);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public R save(@RequestBody LawSubjectTEntity lawSubjectT){
        ValidatorUtils.validateEntity(lawSubjectT, AddGroup.class);
		lawSubjectTService.saveLawSubjectT(lawSubjectT);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @RequestMapping(value ="/update"  ,  method = RequestMethod.POST)
    public R update(@RequestBody LawSubjectTEntity lawSubjectT){
        ValidatorUtils.validateEntity(lawSubjectT, UpdateGroup.class);
		lawSubjectTService.updateLawSubjectT(lawSubjectT);

        return R.ok();
    }

    /**
     * 删除 0-删除，1-默认，2-发布 3-撤销
     */
   /* @ApiOperation("删除")
    @RequestMapping(value ="/delete",method = RequestMethod.GET)
    public R delete(@RequestBody String[] uuids){
		lawSubjectTService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }*/
    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        lawSubjectTService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        lawSubjectTService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();
        fileUtils.download(path, "市场主体名录库模板.xls", response, request);
    }
    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        String message = lawSubjectTService.importLawSubjectT(file);
        return new R().put("message", message);
    }
    /**
     * 全部导出
     */
    @GetMapping(value = "/downloadAll")
    @ApiOperation(value = "全部导出", produces = "application/octet-stream")
    public void downloadAll(HttpServletResponse response) throws Exception {
        List<LawSubjectTEntity> list = lawSubjectTService.downloadAll();
        ExportExcelUtil.exportExcel(list, LawSubjectTEntity.class, "市场主体名录.xls", response);
    }

    /**
     * 批量导出
     */
    @PostMapping("/downloadBatch")
    @ApiOperation(value = "批量导出", produces = "application/octet-stream")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public void downloadBatch(HttpServletResponse response, @RequestBody Map<String, Object> mapIds) throws Exception {
        List<LawSubjectTEntity> list = lawSubjectTService.downloadBatch(mapIds);
        ExportExcelUtil.exportExcel(list, LawSubjectTEntity.class, "市场主体名录.xls", response);
    }

    /**
     * 统计信息
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
        List<LawSubjectTEntity> list = lawSubjectTService.list(new QueryWrapper<LawSubjectTEntity>()
                .ne("DEL_FLAG", "1"));

        //将执法结果时间 格式化到年 放入 lawResultTimeStr
        List<LawSubjectTEntity> processedList = lawSubjectTService.getProcessedData(list);

        // 各省份市场主体情况 统计近五年的年度数据
        List<JSONObject> jsonObjecProvinceWithYearsList = lawSubjectTService.getProvinceWithYearsStatistics(processedList,yearStart,yearEnd);
        // 各省份市场主体情况 统计多一年的年度数据
        List<JSONObject> jsonObjecProvinceWithYearsMoreList = lawSubjectTService.getProvinceWithYearsStatistics(processedList,yearStart -1 ,yearEnd);
        // 各省份市场主体情况 统计近五年的年度数据
        List<JSONObject> jsonObjecProvinceIncreaseWithYearsList = lawSubjectTService.getProvinceIncreaseWithYearsStatistics(jsonObjecProvinceWithYearsMoreList,yearStart,yearEnd);


        JSONObject result = new JSONObject();
        result.put("provinceWithYears",jsonObjecProvinceWithYearsList);
        result.put("provinceIncreaseWithYears",jsonObjecProvinceIncreaseWithYearsList);

        //将数据格式化json
        Map resultJson = new Gson().fromJson(result.toString(), new TypeToken<Map<Object,List<Map<Object,Object>>>>(){}.getType());

        return R.ok().put("data",resultJson);
    }
    
    
    /**
     * 全部发布
     */
    /*@GetMapping("/publishAll")
    @ApiOperation(value = "全部发布", produces = "application/json")
    public R publishAll() {
        lawSubjectTService.commonUpdatePubStatusAll("1");
        return R.ok();
    }*/

    /**
     * 批量发布
     */
    /*@PostMapping("/publishBatch")
    @ApiOperation(value = "批量发布", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R publishBatch(@RequestBody Map<String, Object> mapIds) {
        lawSubjectTService.commonUpdatePubStatusBatch("1", mapIds);
        return R.ok();
    }*/

    /**
     * 全部撤销
     */
    /*@GetMapping("/cancelAll")
    @ApiOperation(value = "全部撤销", produces = "application/json")
    public R cancelAll() {
        lawSubjectTService.commonUpdatePubStatusAll("2");
        return R.ok();
    }*/

    /**
     * 批量撤销
     */
    /*@PostMapping("/cancelBatch")
    @ApiOperation(value = "批量撤销", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R cancelBatch(@RequestBody Map<String, Object> mapIds) {
        lawSubjectTService.commonUpdatePubStatusBatch("2", mapIds);
        return R.ok();
    }*/

    
    
    
}
