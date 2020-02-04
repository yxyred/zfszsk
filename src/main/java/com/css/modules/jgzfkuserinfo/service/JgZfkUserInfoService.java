package com.css.modules.jgzfkuserinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.expert.entity.BumExpertTEntity;
import com.css.modules.jgzfkuserinfo.entity.JgZfkUserInfoEntity;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 执法检查人员库
 *
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
public interface JgZfkUserInfoService extends IService<JgZfkUserInfoEntity> {

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
    void saveJgZfkUserInfo(JgZfkUserInfoEntity announceJgZfkUserInfo);
    /**
     * 修改
     */
    void updateJgZfkUserInfo(JgZfkUserInfoEntity announceJgZfkUserInfo);
    /**
     * 导入excel
     */
    String importJgZfkUserInfo(MultipartFile file) throws Exception;

    /**
     * 下载全部
     * @return
     */
    List<JgZfkUserInfoEntity> downloadAll();

    /**
     * 批量下载
     * @return
     */
    List<JgZfkUserInfoEntity> downloadBatch(Map<String, Object> mapIds);

    /**
     * 按执法年龄段分布
     * @param list
     * @returnl
     */
    List<JSONObject> getAgeGroupStatistics(List<JgZfkUserInfoEntity> list);
    /**
     * 按执法证件的有效性统计
     * @param list
     * @returnl
     */
    List<JSONObject> getIsEffectiveStatistics(List<JgZfkUserInfoEntity> list);
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    List<JgZfkUserInfoEntity> getProcessedData(List<JgZfkUserInfoEntity> list);
    /**
     * 获取按省份名称分组的数据
     * @param list 执法事项list
     * @returnl
     */
    List<JSONObject> getProvinceStatistics(List<JgZfkUserInfoEntity> list);
    /**
     * 根据省份 获取直返人员全部列表
     * @param province 省份
     * @returnl
     */
    List<JgZfkUserInfoEntity> queryByProvince(String province);
    /**
     * 根据省份证号 获取直返人员全部列表
     * @param idNumber 身份证号
     * @returnl
     */
    List<JgZfkUserInfoEntity> queryByIdNumber(String idNumber);

}

