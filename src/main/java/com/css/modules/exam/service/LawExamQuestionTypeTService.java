package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamQuestionTypeTEntity;

import java.util.Map;

/**
 * 试卷关联题目表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamQuestionTypeTService extends IService<LawExamQuestionTypeTEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

