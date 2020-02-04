package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamQuestionTEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 题库信息表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamQuestionTService extends IService<LawExamQuestionTEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 导入excel
     */
    String importLawExamQuestionT(MultipartFile file) throws Exception;
    /**
     * 公用批量修改删除状态方法
     */
    void commonUpdateDelFlagBatch(String pubStatus, Map<String, Object> mapIds);
    /**
     * 公用全部修改删除状态方法
     */
    void commonUpdateDelFlagAll(String delFlag);
}

