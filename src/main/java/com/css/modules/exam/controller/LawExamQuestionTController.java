package com.css.modules.exam.controller;

import java.io.File;
import java.util.Map;

import com.css.common.utils.FileUtils;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.exam.entity.LawExamQuestionTEntity;
import com.css.modules.exam.service.LawExamQuestionTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 题库信息表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamquestiont")
@Api(value = "exam/lawexamquestiont",description = "题库信息管理",tags = "题库信息管理")
public class LawExamQuestionTController {
    @Autowired
    private LawExamQuestionTService lawExamQuestionTService;
    @Autowired
    FileUtils fileUtils;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "题库列表",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "sidx", value = "排序字段", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方式，如：asc、desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "questionTypeId", value = "题型ID", dataType = "string", paramType = "query"),
    })
    public R list(@ApiParam(name = "params", hidden = true)@RequestParam Map<String, Object> params){
        PageUtils page = lawExamQuestionTService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:lawexamquestiont:info")
    public R info(@PathVariable("id") String id){
		LawExamQuestionTEntity lawExamQuestionT = lawExamQuestionTService.getById(id);

        return R.ok().put("lawExamQuestionT", lawExamQuestionT);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加题目", produces = "application/json")
    public R save(@RequestBody LawExamQuestionTEntity lawExamQuestionT){
        if (StringUtils.isNotBlank(lawExamQuestionT.getId())) {
            ValidatorUtils.validateEntity(lawExamQuestionT, AddGroup.class);
            lawExamQuestionT.setId(null);
            lawExamQuestionT.setDelFlag("0");
        }
		lawExamQuestionTService.save(lawExamQuestionT);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:lawexamquestiont:update")
    public R update(@RequestBody LawExamQuestionTEntity lawExamQuestionT){
        ValidatorUtils.validateEntity(lawExamQuestionT, AddGroup.class);
		lawExamQuestionTService.updateById(lawExamQuestionT);

        return R.ok();
    }

  
    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        String message = lawExamQuestionTService.importLawExamQuestionT(file);
        return new R().put("message", message);
    }

    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();

        fileUtils.download(path, "试题模板.xls", response, request);
    }

    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        lawExamQuestionTService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        lawExamQuestionTService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

}
