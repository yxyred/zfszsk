package com.css.modules.jgzskcase.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.FileUtils;
import com.css.common.utils.ExportExcelUtil;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.css.modules.jgzskcase.entity.JgZskCaseEntity;
import com.css.modules.jgzskcase.service.JgZskCaseService;
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
import java.util.List;
import java.util.Map;


/**
 * 执法案例库
 *
 * @author
 * @email
 * @date 2019-11-13 15:59:02
 */
@RestController
@Api(tags = "jgzskcase" ,description = "执法案例库")
@RequestMapping("jgZskCase")
public class JgZskCaseController {
    private static final Logger logger = LoggerFactory.getLogger(JgZskCaseController.class);
    @Autowired
    private JgZskCaseService jgZskCaseService;
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
            @ApiImplicitParam(name = "caseName", value = "案例名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "responsibleUnit", value = "责任单位", dataType = "string", paramType = "query")

    })
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jgZskCaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation("getbyid")
    @RequestMapping(value = "/info/{id}" ,method = RequestMethod.POST)
    public R info(@PathVariable("id") String id){
		JgZskCaseEntity jgZskCase = jgZskCaseService.getById(id);

        return R.ok().put("jgzskcase", jgZskCase);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public R save(@RequestBody JgZskCaseEntity jgZskCase){
        ValidatorUtils.validateEntity(jgZskCase, AddGroup.class);
		jgZskCaseService.saveJgZskCase(jgZskCase);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @RequestMapping(value ="/update"  ,  method = RequestMethod.POST)
    public R update(@RequestBody JgZskCaseEntity jgZskCase){
        ValidatorUtils.validateEntity(jgZskCase, UpdateGroup.class);
		jgZskCaseService.updateJgZskCase(jgZskCase);
        return R.ok();
    }

    /**
     * 删除 0-删除，1-默认，2-发布 3-撤销
     */
   /* @ApiOperation("删除")
    @RequestMapping(value ="/delete",method = RequestMethod.GET)
    public R delete(@RequestBody String[] uuids){
		jgZskCaseService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }*/
    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        jgZskCaseService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        jgZskCaseService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }
    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();

        fileUtils.download(path, "执法案例库模板.xls", response, request);
    }
    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        String message = jgZskCaseService.importJgZskCase(file);
        return new R().put("message", message);
    }
    /**
     * 全部导出
     */
    @GetMapping(value = "/downloadAll")
    @ApiOperation(value = "全部导出", produces = "application/octet-stream")
    public void downloadAll(HttpServletResponse response) throws Exception {
        List<JgZskCaseEntity> list = jgZskCaseService.downloadAll();
        ExportExcelUtil.exportExcel(list, JgZskCaseEntity.class, "执法案例库.xls", response);
    }

    /**
     * 批量导出
     */
    @PostMapping("/downloadBatch")
    @ApiOperation(value = "批量导出", produces = "application/octet-stream")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public void downloadBatch(HttpServletResponse response, @RequestBody Map<String, Object> mapIds) throws Exception {
//      直接获取map中存的数组map={"ids":[1,2,3]}
        List<Object> vlist = (List<Object>) mapIds.get("ids");
//      list 转化为数组
        Object[] uuids = vlist.toArray(new Object[vlist.size()]);
//      Object... values代表Object[]
        List<JgZskCaseEntity> list = jgZskCaseService.list(new QueryWrapper<JgZskCaseEntity>()
                .in(!mapIds.isEmpty(), "id", uuids)
                .ne("DEL_FLAG", "1"));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
        }
        ExportExcelUtil.exportExcel(list, JgZskCaseEntity.class, "执法案例库.xls", response);
    }


    /**
     * 设为典型
     */
    @PostMapping("/toTypicalCase")
    @ApiOperation(value = "设为典型", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R toTypicalCase(@RequestBody Map<String, Object> mapIds) {
        jgZskCaseService.UpdateTypicalCaseBatch("1", mapIds);
        return R.ok();
    }
    /**
     * 批量撤销
     */
    @PostMapping("/undoTypicalCase")
    @ApiOperation(value = "取消典型", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R undoTypicalCase(@RequestBody Map<String, Object> mapIds) {
        jgZskCaseService.UpdateTypicalCaseBatch("0", mapIds);
        return R.ok();
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
        List<JgZskCaseEntity> list = jgZskCaseService.list(new QueryWrapper<JgZskCaseEntity>()
                .ne("DEL_FLAG", "1"));

        //将结案日期 格式化到年 放入 settlementDateStr
        List<JgZskCaseEntity> processedList = jgZskCaseService.getProcessedData(list);

        //按执法类别分组的数据,带百分比
        List<JSONObject> jsonObjectLawTypeNameWithProportionList = jgZskCaseService.getLawTypeNameStatistics(processedList);

        //按执法结果日期分组的数据，不带百分比
        List<JSONObject> jsonObjectSettlementDateList = jgZskCaseService.getSettlementDateStrStatistics(processedList,yearStart,yearEnd);
        //按执法类别 统计近五年的年度数据
        List<JSONObject> jsonObjectLawTypeNameWithYearsList = jgZskCaseService.getLawTypeNameWithYearsStatistics(processedList,yearStart,yearEnd);


        JSONObject result = new JSONObject();
        result.put("lawTypeNameWithProportion",jsonObjectLawTypeNameWithProportionList);
        result.put("settlementDate",jsonObjectSettlementDateList);
        result.put("lawTypeNameWithYears",jsonObjectLawTypeNameWithYearsList);

        //将数据格式化json
        Map resultJson = new Gson().fromJson(result.toString(), new TypeToken<Map<Object,List<Map<Object,Object>>>>(){}.getType());

        return R.ok().put("data",resultJson);
    }




}
