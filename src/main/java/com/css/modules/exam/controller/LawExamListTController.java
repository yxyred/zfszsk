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

import com.css.modules.exam.entity.LawExamListTEntity;
import com.css.modules.exam.service.LawExamListTService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.R;



/**
 * 考生名单关联表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@RestController
@RequestMapping("exam/lawexamlistt")
public class LawExamListTController {
    @Autowired
    private LawExamListTService lawExamListTService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:lawexamlistt:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lawExamListTService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:lawexamlistt:info")
    public R info(@PathVariable("id") String id){
		LawExamListTEntity lawExamListT = lawExamListTService.getById(id);

        return R.ok().put("lawExamListT", lawExamListT);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:lawexamlistt:save")
    public R save(@RequestBody LawExamListTEntity lawExamListT){
		lawExamListTService.save(lawExamListT);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:lawexamlistt:update")
    public R update(@RequestBody LawExamListTEntity lawExamListT){
		lawExamListTService.updateById(lawExamListT);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:lawexamlistt:delete")
    public R delete(@RequestBody String[] ids){
		lawExamListTService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
