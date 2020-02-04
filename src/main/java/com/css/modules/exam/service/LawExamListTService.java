package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamListTEntity;

import java.util.Map;

/**
 * 考生名单关联表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamListTService extends IService<LawExamListTEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

