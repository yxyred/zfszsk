package com.css.modules.jgzskcase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.jgzskcase.entity.JgZskCaseEntity;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 执法案例库
 *
 * @author yanxingyu
 * @email
 * @date 2019-11-13 15:59:02
 */
public interface JgZskCaseService extends IService<JgZskCaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
    String importJgZskCase(MultipartFile file) throws Exception;
    void saveJgZskCase(JgZskCaseEntity jgZskCaseEntity);

    void updateJgZskCase(JgZskCaseEntity jgZskCaseEntity);
    /**
     * 公用批量修改删除状态方法
     */
    void commonUpdateDelFlagBatch(String delFlag, Map<String, Object> mapIds);
    /**
     * 公用全部修改删除状态方法
     */
    void commonUpdateDelFlagAll(String delFlag);

    /**
     * 设为典型
     */
    void UpdateTypicalCaseBatch(String typicalCase, Map<String, Object> mapIds);
    /**
     * 按执法类别分组的数据,带百分比
     * @param list
     * @returnl
     */
    List<JSONObject> getLawTypeNameStatistics(List<JgZskCaseEntity> list);
    /**
     * 按执法结果日期分组的数据，不带百分比
     * @param list
     * @returnl
     */
    List<JSONObject> getSettlementDateStrStatistics(List<JgZskCaseEntity> list,int yearStart,int yearEnd);

    /**
     * 按执法类别 统计近五年的年度数据
     * @param list
     * @param yearStart
     * @param yearEnd
     * @return
     */
    List<JSONObject> getLawTypeNameWithYearsStatistics(List<JgZskCaseEntity> list,int yearStart,int yearEnd);
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    List<JgZskCaseEntity> getProcessedData(List<JgZskCaseEntity> list);

    /**
     * 下载全部
     * @return
     */
    List<JgZskCaseEntity> downloadAll();
}

