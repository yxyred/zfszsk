package com.css.modules.exam.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.css.modules.exam.entity.LawExamUserAnswerTEntity;
import com.css.modules.exam.service.LawExamUserAnswerTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;



/**
 * 考生答案表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:20
 */
@RestController
@RequestMapping("exam/lawexamuseranswert")
@Api(value = "exam/lawexamuseranswert", description = "考生答案表", tags = "考生答案表")
public class LawExamUserAnswerTController {
    @Autowired
    private LawExamUserAnswerTService lawExamUserAnswerTService;
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
    })
    public R list(@ApiParam(name="params" ,hidden = true)@RequestParam Map<String, Object> params){
        PageUtils page = lawExamUserAnswerTService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 提交试卷
     */
    @PostMapping("/save")
    @ApiOperation(value = "提交试卷", produces = "application/json")
    public R save(@RequestBody List<LawExamUserAnswerTEntity> lawExamUserAnswerTList){
		String message = lawExamUserAnswerTService.saveUserAnswer(lawExamUserAnswerTList);

        return R.ok().put("message",message);
    }

}
