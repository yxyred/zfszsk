package com.css.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.exam.entity.LawExamQuestionTEntity;
import com.css.modules.exam.entity.LawExamTestTEntity;

import java.util.List;
import java.util.Map;

/**
 * 试卷信息表
 *
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
public interface LawExamTestTService extends IService<LawExamTestTEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 考生参加的考试列表
     * @param params
     * @return
     */
    PageUtils examTestQueryPage(Map<String, Object> params);
    /**
     * 生成试卷
     */
    String saveLawExamTestT(LawExamTestTEntity lawExamTestT);

    /**
     * 获取服务器当前时间与考试结束时间的差值
     * @param testId
     * @return
     */
    long calculateTestTimeGapSecond(String testId);


    /**
     * 获取试卷详情 的 全部题型信息 不包含答案 用于考生考试
     * @param testId 试卷ID
     */
    Map getInfo(String testId);
    /**
     * 获取试卷详情 的 指定题型的信息 包含答案 用于考生考试
     * @param testId 试卷ID
     * @param examQuestionTypeId 试题类型code
     */
    Map getInfo(String testId,String ...examQuestionTypeId);

    /**
     * 传入试题的list 根据试题类型 添加 试题的分数和题型名称  信息
     * @param LawExamQuestionTList 试题的list
     */
    List<LawExamQuestionTEntity> getDataByQuestionType(List <LawExamQuestionTEntity> LawExamQuestionTList);
    /**
     * 获取自动判分的题型的信息
     * name(题型名称) code(题型code) examScore(分值) sysFlag(是否自动判分的标识)
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    List getSysExamQuestionTypeList();
    /**
     * 获取人工判分的题型的信息
     * name(题型名称) code(题型code) examScore(分值) sysFlag(是否自动判分的标识)
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    List getSynExamQuestionTypeList();

    /**
     * 公用批量修改状态状态方法
     */
    void commonStatusBatch(String status, Map<String, Object> mapIds);
    /**
     * 公用全部修改状态状态方法
     */
    void commonStatusAll(String status);
    /**
     * 公用批量修改删除状态方法
     */
    void commonUpdateDelFlagBatch(String status, Map<String, Object> mapIds);
    /**
     * 公用全部修改删除状态方法
     */
    void commonUpdateDelFlagAll(String delFlag);
}

