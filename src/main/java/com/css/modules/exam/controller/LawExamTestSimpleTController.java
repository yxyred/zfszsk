package com.css.modules.exam.controller;

import com.css.common.utils.R;
import com.css.modules.exam.entity.LawExamTestSimpleTEntity;
import com.css.modules.exam.service.LawExamTestSimpleTService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
public class LawExamTestSimpleTController {
    @Autowired
    private LawExamTestSimpleTService lawExamTestTService;

    /**
     * 获取考试列表 name和id
     * @return
     */
    @GetMapping("/simpleListAll")
    @ApiOperation(value = "获取考试列表 name和id",
            produces = "application/json")
    public R simpleListAll(){
        List<LawExamTestSimpleTEntity> list = lawExamTestTService.simpleListAll();
        return R.ok().put("page", list);
    }


    /**
     * 获取考试列表 name和id
     * 不返回 已经使用的
     * @return
     */
    @GetMapping("/simpleListAllWithoutUsed")
    @ApiOperation(value = "获取考试列表 name和id 不返回 已经使用的",
            produces = "application/json")
    public R simpleListAllWithoutUsed(){
        List<LawExamTestSimpleTEntity> list = lawExamTestTService.simpleListAllWithoutUsed();
        return R.ok().put("page", list);
    }

}
