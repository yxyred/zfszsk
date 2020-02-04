package com.css.modules.exam.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css.common.utils.FileUtils;
import com.css.common.utils.ExportExcelUtil;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.exam.entity.LawExamUserTEntity;
import com.css.modules.exam.service.LawExamUserTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 考生信息表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:20
 */
@RestController
@RequestMapping("exam/lawexamusert")
@Api(value = "exam/lawexamusert", description = "考生信息表", tags = "考生信息表")
public class LawExamUserTController {
    @Autowired
    private LawExamUserTService lawExamUserTService;
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
            @ApiImplicitParam(name = "name", value = "考生姓名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "testName", value = "试卷名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "isQualified", value = "是否合格", dataType = "string", paramType = "query"),
    })
    public R list(@ApiParam(name="params" ,hidden = true)@RequestParam Map<String, Object> params){
        PageUtils page = lawExamUserTService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 导入excel
     */
    @PostMapping("/import")
    @ApiOperation(value = "导入excel", produces = "application/json")
    public R importExcel(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        String message = lawExamUserTService.importLawExamUserT(file);
        return new R().put("message", message);
    }

    /**
     * 导入excel前计算考生人数
     */
    @PostMapping("/count")
    @ApiOperation(value = "导入excel前计算考生人数", produces = "application/json")
    public R count(@ApiParam(value = "文件",required = true) @RequestParam("file") MultipartFile file) throws Exception {
        List<LawExamUserTEntity> excelLawExamUserTlist = ExportExcelUtil.importExcel(file, 0, 1, LawExamUserTEntity.class);
        int count = excelLawExamUserTlist.size();
        return new R().put("data", count);
    }



    /**
     * 全部导出
     */
    @GetMapping(value = "/downloadAll")
    @ApiOperation(value = "全部导出", produces = "application/octet-stream")
    public void downloadAll(HttpServletResponse response) throws Exception {
        List<LawExamUserTEntity> list = lawExamUserTService.list(new QueryWrapper<LawExamUserTEntity>()
                .ne("DEL_FLAG", "1"));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
        }
        ExportExcelUtil.exportExcel(list, LawExamUserTEntity.class, "考生信息.xls", response);
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
        Object[] ids = vlist.toArray(new Object[vlist.size()]);
//      Object... values代表Object[]
        List<LawExamUserTEntity> list = lawExamUserTService.list(new QueryWrapper<LawExamUserTEntity>()
                .in(!mapIds.isEmpty(), "id", ids)
                .ne("DEL_FLAG", "1"));
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
        }
        ExportExcelUtil.exportExcel(list, LawExamUserTEntity.class, "考生信息.xls", response);
    }

    /**
     * 下载模板
     */
    @GetMapping(value = "/downloadDemo")
    @ApiOperation(value = "下载模板", produces = "application/octet-stream")
    public void downloadDemo(HttpServletResponse response, HttpServletRequest request) {
        String path = fileUtils.getDocRootPath();

        fileUtils.download(path, "考生信息模板.xls", response, request);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:lawexamusert:info")
    public R info(@PathVariable("id") String id){
		LawExamUserTEntity lawExamUserT = lawExamUserTService.getById(id);

        return R.ok().put("lawExamUserT", lawExamUserT);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存", produces = "application/json")
    public R save(@RequestBody LawExamUserTEntity lawExamUserT){
        ValidatorUtils.validateEntity(lawExamUserT, AddGroup.class);
        lawExamUserTService.save(lawExamUserT);
        return R.ok();
    }

    /**
     * 修改  联系方式前的数据
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改  联系方式前的数据", produces = "application/json")
    public R update(@RequestBody LawExamUserTEntity lawExamUserT){
        ValidatorUtils.validateEntity(lawExamUserT, UpdateGroup.class);
        lawExamUserTService.updateById(lawExamUserT);
        return R.ok();
    }

    /**
     * 全部删除
     */
    @GetMapping("/deleteAll")
    @ApiOperation(value = "全部删除", produces = "application/json")
    public R deleteAll() {
        lawExamUserTService.commonUpdateDelFlagAll("1");
        return R.ok();
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除", produces = "application/json")
    @ApiImplicitParam(name = "mapIds",value = "ids",paramType = "body",dataType = "body")
    public R deleteBatch(@RequestBody Map<String, Object> mapIds) {
        lawExamUserTService.commonUpdateDelFlagBatch("1", mapIds);
        return R.ok();
    }

}
