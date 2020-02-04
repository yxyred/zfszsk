package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.modules.exam.entity.LawExamTestSimpleTEntity;

import java.util.List;

/**
 * 试卷信息表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamTestSimpleTService extends IService<LawExamTestSimpleTEntity> {
    /**
     * 获取考试列表 name和id
     * @return
     */
    List<LawExamTestSimpleTEntity> simpleListAll();
    /**
     * 获取考试列表 name和id
     * 不返回 已经使用的
     * @return
     */
    List<LawExamTestSimpleTEntity> simpleListAllWithoutUsed();
}

