package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamPlanTEntity;
import com.css.modules.exam.entity.LawExamUserTEntity;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 考生信息表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:20
 */
public interface LawExamUserTService extends IService<LawExamUserTEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 导入excel 同时 添加考试计划相关信息 和 试卷信息
     */
    String importLawExamUserTByExamPlan(MultipartFile file, LawExamPlanTEntity lawExamPlanT) throws Exception;
    /**
     * 导入excel
     */
    String importLawExamUserT(MultipartFile file) throws Exception;
    /**
     * 公用批量修改删除状态方法
     */
    void commonUpdateDelFlagBatch(String pubStatus, Map<String, Object> mapIds);
    /**
     * 公用全部修改删除状态方法
     */
    void commonUpdateDelFlagAll(String delFlag);
}

