package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamCorrectTEntity;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 考试批改表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamCorrectTService extends IService<LawExamCorrectTEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取考生答案 案例解析题部分
     * @param correctId 传入试卷批改ID
     */
    Map getSynInfo(String correctId);

    /**
     * 人工批改提交
     * @param params
     */
    String synSave(Map<String, Object> params);

    /**
     * 批量同步信息
     * @param mapIds
     */
    String sysncInfoBatch(Map<String, Object> mapIds);
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    List<LawExamCorrectTEntity> getProcessedData(List<LawExamCorrectTEntity> list);
    /**
     * 历年考试结果统计
     * @param list
     * @returnl
     */
    List<JSONObject> getExamTimeStrStatistics(List<LawExamCorrectTEntity> list, int yearStart, int yearEnd);

 /**
     * 按试卷名称 考试结果统计
     * @param list
     * @returnl
     */
    List<JSONObject> getTestNameStrStatistics(List<LawExamCorrectTEntity> list, int yearStart, int yearEnd);


}

