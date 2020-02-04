package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamNoticeTEntity;

import java.util.Map;

/**
 * 考试须知
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamNoticeTService extends IService<LawExamNoticeTEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 考试须知是否已读
     */
    void readNotice();
}

