package com.css.modules.exam.controller;

import java.util.Date;
import java.util.Map;

import com.css.common.utils.DateUtils;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.exam.entity.LawExamTestTEntity;
import com.css.modules.exam.service.LawExamTestTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;



/**
 * 试卷信息表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamtestt")
@Api(value = "exam/lawexamtestt", description = "试卷信息管理", tags = "试卷信息管理")
public class LawExamTestTController {
    @Autowired
    private LawExamTestTService lawExamTestTService;

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
            @ApiImplicitParam(name = "name", value = "试卷名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "试卷分类", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "string", paramType = "query"),
    })
    public R list(@ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params){
        PageUtils page = lawExamTestTService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 试卷信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "试卷信息", produces = "application/json")
    public R info(@PathVariable("id") String id){
        Map result = lawExamTestTService.getInfo(id);

        return R.ok().put("data", result);
    }

    /**
     * 考生参加的考试列表
     */
    @GetMapping("/examTestlist")
    @ApiOperation(value = "考生参加的考试列表", notes = "返回考生参加的考试列表",
            produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "testName", value = "试卷名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "examTime", value = "考试日期", dataType = "date", paramType = "query"),
    })
    public R examTestlist(@ApiParam(name="params" ,hidden = true) @RequestParam Map<String, Object> params){
        PageUtils page = lawExamTestTService.examTestQueryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 获取服务器当前时间与考试结束时间的差值
     */
    @PostMapping("/calculateTimeGapSecond")
    @ApiOperation(value = "获取服务器当前时间与考试结束时间的差值", notes = "返回获取服务器当前时间与考试结束时间的差值",
            produces = "application/json")
    @ApiImplicitParam(name = "testId",value = "testId",paramType = "body",dataType = "body")
    public R calculateTestTimeGapSecond(@RequestBody String testId){
        long second = lawExamTestTService.calculateTestTimeGapSecond(testId);


        return R.ok().put("data", second);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "生成试卷", produces = "application/json")
    public R save(@RequestBody LawExamTestTEntity lawExamTestT){
        ValidatorUtils.validateEntity(lawExamTestT, AddGroup.class);
		String message = lawExamTestTService.saveLawExamTestT(lawExamTestT);
        return R.ok().put("message",message);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:lawexamtestt:update")
    public R update(@RequestBody LawExamTestTEntity lawExamTestT){
        ValidatorUtils.validateEntity(lawExamTestT, AddGroup.class);
		lawExamTestTService.updateById(lawExamTestT);
        return R.ok();
    }
    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        lawExamTestTService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        lawExamTestTService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

    /**
     * 全部修改状态
     */
    @GetMapping("/publishAll")
    @ApiOperation(value = "全部修改状态", produces = "application/json")
    public R publishAll() {
        lawExamTestTService.commonStatusAll("1");
        return R.ok();
    }

    /**
     * 批量修改状态
     */
    @PostMapping("/publishBatch")
    @ApiOperation(value = "批量修改状态", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R publishBatch(@RequestBody Map<String, Object> mapIds) {
        lawExamTestTService.commonStatusBatch("1", mapIds);
        return R.ok();
    }
}
