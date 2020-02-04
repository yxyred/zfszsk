package com.css.modules.expert.controller;

import java.util.Arrays;
import java.util.Map;

import com.css.common.utils.PageUtils;
import com.css.common.utils.R;
import com.css.modules.expert.entity.BumExpertExperienceTEntity;
import com.css.modules.expert.service.BumExpertExperienceTService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 专家辅法经历表
 *
 * @author liukai
 * @date 2019-11-18 14:44:19
 */
@RestController
@RequestMapping("generator/bumexpertexperiencet")
public class BumExpertExperienceTController {
    @Autowired
    private BumExpertExperienceTService bumExpertExperienceTService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("generator:bumexpertexperiencet:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = bumExpertExperienceTService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("generator:bumexpertexperiencet:info")
    public R info(@PathVariable("id") String id){
		BumExpertExperienceTEntity bumExpertExperienceT = bumExpertExperienceTService.getById(id);

        return R.ok().put("bumExpertExperienceT", bumExpertExperienceT);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("generator:bumexpertexperiencet:save")
    public R save(@RequestBody BumExpertExperienceTEntity bumExpertExperienceT){
		bumExpertExperienceTService.save(bumExpertExperienceT);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("generator:bumexpertexperiencet:update")
    public R update(@RequestBody BumExpertExperienceTEntity bumExpertExperienceT){
		bumExpertExperienceTService.updateById(bumExpertExperienceT);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("generator:bumexpertexperiencet:delete")
    public R delete(@RequestBody String[] ids){
		bumExpertExperienceTService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
