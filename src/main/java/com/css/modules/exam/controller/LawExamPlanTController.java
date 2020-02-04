package com.css.modules.exam.controller;

import java.util.*;

import com.css.common.utils.FileUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.exam.entity.LawExamPlanTEntity;
import com.css.modules.exam.service.LawExamPlanTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 考试计划表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamplant")
@Api(value = "exam/lawexamplant", description = "考试计划表", tags = "考试计划表")
public class LawExamPlanTController {
    @Autowired
    private LawExamPlanTService lawExamPlanTService;
    @Autowired
    FileUtils fileUtils;
    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "试卷信息列表", notes = "返回试卷信息列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "testName", value = "试卷名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "examTime", value = "考试日期", dataType = "date", paramType = "query"),
    })
    public R list(@ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params){
        PageUtils page = lawExamPlanTService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     *  生成考试计划
     */
    @PostMapping("/createTestPlan")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "考试计划ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "testId", value = "试卷ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "testName", value = "试卷名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "examTime", value = "考试日期", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(name = "examStartTime", value = "考试开始时间", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(name = "examEndTime", value = "考试结束时间", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(name = "limitTime", value = "答题时长", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "listId", value = "考生名单ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "listUrl", value = "考生名单URL", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "joinNum", value = "考生人数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query")
    })
    @ApiOperation(value = "生成考试计划", produces = "application/json")
    public R createTestPlan(  @ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file,
                          @ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params
    ) throws Exception {
       String message = lawExamPlanTService.createTestPlan(file,params);

        return R.ok().put("message", message);
    }


    /**
     * 修改考试计划
     */
    @PostMapping("/updateTesPlan")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "考试计划ID", dataType = "String", paramType = "query",required = true),
            @ApiImplicitParam(name = "testId", value = "试卷ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "testName", value = "试卷名称", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "examTime", value = "考试日期", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(name = "examStartTime", value = "考试开始时间", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(name = "examEndTime", value = "考试结束时间", dataType = "date", paramType = "query", required = true),
            @ApiImplicitParam(name = "limitTime", value = "答题时长", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "listId", value = "考生名单ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "listUrl", value = "考生名单URL", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "joinNum", value = "考生人数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query")
    })
    @ApiOperation(value = "生成考试计划", produces = "application/json")
    public R updateTesPlan(@ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params
    ) throws Exception {

        String message = lawExamPlanTService.updateTesPlan(params);

        return R.ok().put("message", message);
    }

    /**
     * 下载考生名单
     */
    @GetMapping(value = "/downloadExamUser/{fileName}")
    @ApiOperation(value = "下载考生名单", produces = "application/octet-stream")
    public void downloadExamUser(@PathVariable("fileName") String fileName,HttpServletResponse response, HttpServletRequest request) {
        fileUtils.download(fileName, response);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "信息", produces = "application/json")
    public R info(@PathVariable("id") String id){
		LawExamPlanTEntity lawExamPlanT = lawExamPlanTService.getById(id);

        return R.ok().put("lawExamPlanT", lawExamPlanT);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存", produces = "application/json")
    public R save(@RequestBody LawExamPlanTEntity lawExamPlanT){
		lawExamPlanTService.save(lawExamPlanT);

        return R.ok();
    }

    /**
     * 通知考生
     */
    @PostMapping("/pushMessage")
    @ApiOperation(value = "通知考生", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R pushMessage(@RequestBody Map<String, Object> mapIds) {
        String message = lawExamPlanTService.pushMessage(mapIds);
        return R.ok().put("msg",message);
    }
    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        lawExamPlanTService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        lawExamPlanTService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

}
