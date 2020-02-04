package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamRelateTEntity;

import java.util.List;
import java.util.Map;

/**
 * 试卷关联题型表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamRelateTService extends IService<LawExamRelateTEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据试卷 获取该试卷 与题库 对应的 所有题的ID
     * @param testId
     * @return
     */
    List getQuestionIds(String testId);

    /**
     * 根据试卷 获取该试卷 与题库 对应的 所有题的ID和题目编号
     * @param testId
     * @return
     */
    List getQuestionIdsAndQuestionNum(String testId);


}

