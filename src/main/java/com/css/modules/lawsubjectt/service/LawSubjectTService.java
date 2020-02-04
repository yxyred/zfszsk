package com.css.modules.lawsubjectt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.jgzfkuserinfo.entity.JgZfkUserInfoEntity;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import com.css.modules.lawsubjectt.entity.LawSubjectTEntity;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 市场主体名录表
 *
 * @author
 * @email
 * @date 2019-11-18 14:26:25
 */
public interface LawSubjectTService extends IService<LawSubjectTEntity> {

    PageUtils queryPage(Map<String, Object> params);
    String importLawSubjectT(MultipartFile file) throws Exception;
    /**
     * 下载全部
     * @return
     */
    List<LawSubjectTEntity> downloadAll();

    /**
     * 批量下载
     * @return
     */
    List<LawSubjectTEntity> downloadBatch(Map<String, Object> mapIds);
    /**
     * 公用批量修改删除状态方法
     */
    void commonUpdateDelFlagBatch(String delFlag, Map<String, Object> mapIds);
    /**
     * 公用全部修改删除状态方法
     */
    void commonUpdateDelFlagAll(String delFlag);
    /**
     * 公用批量修改发布状态方法
     */
    void commonUpdatePubStatusBatch(String pubStatus, Map<String, Object> mapIds);
    /**
     * 公用全部修改发布状态方法
     */
    void commonUpdatePubStatusAll(String pubStatus);
    /**
     * 按省份名称 统计近五年的年度数据
     * @param list
     * @param yearStart
     * @param yearEnd
     * @return
     */
    List<JSONObject> getProvinceWithYearsStatistics(List<LawSubjectTEntity> list, int yearStart, int yearEnd);
 /**
     * 按省份名称 统计近五年的年度数据
     * @param list
     * @param yearStart
     * @param yearEnd
     * @return
     */
    List<JSONObject> getProvinceIncreaseWithYearsStatistics(List<JSONObject> jsonObjecProvinceWithYearsList,int yearStart,int yearEnd);

    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    List<LawSubjectTEntity> getProcessedData(List<LawSubjectTEntity> list);

    void saveLawSubjectT(LawSubjectTEntity lawSubjectTEntity);

    void updateLawSubjectT(LawSubjectTEntity lawSubjectTEntity);

    /**
     * 通过uniscid远程调用
     * @param uniscid
     */
    LawSubjectTEntity getRemoteSubjectByUniscid(String uniscid);
}

