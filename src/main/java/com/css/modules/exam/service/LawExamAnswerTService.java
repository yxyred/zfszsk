package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamAnswerTEntity;

import java.util.Map;

/**
 * 题目答案表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:18
 */
public interface LawExamAnswerTService extends IService<LawExamAnswerTEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

