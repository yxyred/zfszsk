package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamPlanTEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 考试计划表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamPlanTService extends IService<LawExamPlanTEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 根据考试日期 和 具体时间 改变返回前段的考试状态 并 将状态在数据库中存储
     * @param lawExamPlanTEntities
     * @return
     */
    void updateExamPlanStatus(List<LawExamPlanTEntity> lawExamPlanTEntities);
    /**
     *  传入文件 生成试卷 和 修改试卷
     * @param file
     * @param params
     * @return
     */
    String createTestPlan(MultipartFile file,Map<String, Object> params) throws Exception;
    /**
     *  不传文件 修改试卷
     * @param params
     * @return
     */
    String updateTesPlan(Map<String, Object> params) throws Exception;

    /**
     * 通知考生
     */
    String pushMessage(Map<String, Object> mapIds);
    /**
     * 公用批量修改删除状态方法
     */
    void commonUpdateDelFlagBatch(String status, Map<String, Object> mapIds);
    /**
     * 公用全部修改删除状态方法
     */
    void commonUpdateDelFlagAll(String delFlag);
}

