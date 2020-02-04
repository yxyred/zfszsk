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

import com.css.modules.exam.entity.LawExamRelateTEntity;
import com.css.modules.exam.service.LawExamRelateTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;



/**
 * 试卷关联题型表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamrelatet")
public class LawExamRelateTController {
    @Autowired
    private LawExamRelateTService lawExamRelateTService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:lawexamrelatet:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lawExamRelateTService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:lawexamrelatet:info")
    public R info(@PathVariable("id") String id){
		LawExamRelateTEntity lawExamRelateT = lawExamRelateTService.getById(id);

        return R.ok().put("lawExamRelateT", lawExamRelateT);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:lawexamrelatet:save")
    public R save(@RequestBody LawExamRelateTEntity lawExamRelateT){
		lawExamRelateTService.save(lawExamRelateT);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:lawexamrelatet:update")
    public R update(@RequestBody LawExamRelateTEntity lawExamRelateT){
		lawExamRelateTService.updateById(lawExamRelateT);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:lawexamrelatet:delete")
    public R delete(@RequestBody String[] ids){
		lawExamRelateTService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
