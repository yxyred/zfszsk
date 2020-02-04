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

import com.css.modules.exam.entity.LawExamAnswerTEntity;
import com.css.modules.exam.service.LawExamAnswerTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;



/**
 * 题目答案表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:18
 */
@RestController
@RequestMapping("exam/lawexamanswert")
public class LawExamAnswerTController {
    @Autowired
    private LawExamAnswerTService lawExamAnswerTService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:lawexamanswert:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lawExamAnswerTService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:lawexamanswert:info")
    public R info(@PathVariable("id") String id){
		LawExamAnswerTEntity lawExamAnswerT = lawExamAnswerTService.getById(id);

        return R.ok().put("lawExamAnswerT", lawExamAnswerT);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:lawexamanswert:save")
    public R save(@RequestBody LawExamAnswerTEntity lawExamAnswerT){
		lawExamAnswerTService.save(lawExamAnswerT);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:lawexamanswert:update")
    public R update(@RequestBody LawExamAnswerTEntity lawExamAnswerT){
		lawExamAnswerTService.updateById(lawExamAnswerT);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:lawexamanswert:delete")
    public R delete(@RequestBody String[] ids){
		lawExamAnswerTService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
