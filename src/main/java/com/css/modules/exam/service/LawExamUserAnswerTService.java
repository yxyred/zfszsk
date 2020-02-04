package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamUserAnswerTEntity;

import java.util.List;
import java.util.Map;

/**
 * 考生答案表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:20
 */
public interface LawExamUserAnswerTService extends IService<LawExamUserAnswerTEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存考生答案
     * @param lawExamUserAnswerTList
     * @return
     */
    String saveUserAnswer(List<LawExamUserAnswerTEntity> lawExamUserAnswerTList);
    /**
     * 根据考试批次ID 和题型ID 获取 答案
     * @param correctId
     * @param examQuestionTypeId
     * @return
     */
    List<LawExamUserAnswerTEntity> getSynAnswer(String correctId,String ...examQuestionTypeId);
}

