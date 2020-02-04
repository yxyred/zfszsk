package com.css.modules.exam.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.css.modules.exam.entity.LawExamQuestionTypeTEntity;
import com.css.modules.exam.service.LawExamQuestionTypeTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;



/**
 * 试卷关联题目表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamquestiontypet")
public class LawExamQuestionTypeTController {
    @Autowired
    private LawExamQuestionTypeTService lawExamQuestionTypeTService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:lawexamquestiontypet:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lawExamQuestionTypeTService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:lawexamquestiontypet:info")
    public R info(@PathVariable("id") String id){
		LawExamQuestionTypeTEntity lawExamQuestionTypeT = lawExamQuestionTypeTService.getById(id);

        return R.ok().put("lawExamQuestionTypeT", lawExamQuestionTypeT);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:lawexamquestiontypet:save")
    public R save(@RequestBody LawExamQuestionTypeTEntity lawExamQuestionTypeT){
		lawExamQuestionTypeTService.save(lawExamQuestionTypeT);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:lawexamquestiontypet:update")
    public R update(@RequestBody LawExamQuestionTypeTEntity lawExamQuestionTypeT){
		lawExamQuestionTypeTService.updateById(lawExamQuestionTypeT);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:lawexamquestiontypet:delete")
    public R delete(@RequestBody String[] ids){
		lawExamQuestionTypeTService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
