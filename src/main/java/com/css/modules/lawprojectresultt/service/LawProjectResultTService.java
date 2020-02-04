package com.css.modules.lawprojectresultt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.jgzfkuserinfo.entity.JgZfkUserInfoEntity;
import com.css.modules.jgzskcase.entity.JgZskCaseEntity;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 执法结果信息
 *
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
public interface LawProjectResultTService extends IService<LawProjectResultTEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 公用批量修改发布状态方法
     */
    void commonUpdatePubStatusBatch(String pubStatus, Map<String, Object> mapIds);
    /**
     * 公用全部修改发布状态方法
     */
    void commonUpdatePubStatusAll(String pubStatus);
    /**
     * 公用批量修改删除状态方法
     */
    void commonUpdateDelFlagBatch(String pubStatus, Map<String, Object> mapIds);
    /**
     * 公用全部修改删除状态方法
     */
    void commonUpdateDelFlagAll(String delFlag);
    /**
     * 保存
     */
    void saveLawProjectResultT(LawProjectResultTEntity announceLawProjectResultT);
    /**
     * 修改
     */
    void updateLawProjectResultT(LawProjectResultTEntity announceLawProjectResultT);
    /**
     * 导入excel
     */
    String importLawProjectResultT(MultipartFile file) throws Exception;
    /**
     * 下载全部
     * @return
     */
    List<LawProjectResultTEntity> downloadAll();

    /**
     * 批量下载
     * @return
     */
    List<LawProjectResultTEntity> downloadBatch(Map<String, Object> mapIds);
    /**
     * 按执法类别分组的数据,带百分比
     * @param list
     * @returnl
     */
    List<JSONObject> getLawTypeNameStatistics(List<LawProjectResultTEntity> list);
    /**
     * 按执法结果日期分组的数据，不带百分比
     * @param list
     * @returnl
     */
    List<JSONObject> getLawResultTimeStrStatistics(List<LawProjectResultTEntity> list,int yearStart,int yearEnd);

    /**
     * 按执法类别 统计近五年的年度数据
     * @param list
     * @param yearStart
     * @param yearEnd
     * @return
     */
    List<JSONObject> getLawTypeNameWithYearsStatistics(List<LawProjectResultTEntity> list,int yearStart,int yearEnd);
    /**
     * 获取按省份名称分组的数据
     * @param list 执法事项list
     * @returnl
     */
    List<JSONObject> getProvinceStatistics(List<LawProjectResultTEntity> list);
    /**
     * 按省份名称 统计近五年的年度数据
     * @param list
     * @param yearStart
     * @param yearEnd
     * @return
     */
    List<JSONObject> getProvinceWithYearsStatistics(List<LawProjectResultTEntity> list, int yearStart, int yearEnd);

    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    List<LawProjectResultTEntity> getProcessedData(List<LawProjectResultTEntity> list);

}

