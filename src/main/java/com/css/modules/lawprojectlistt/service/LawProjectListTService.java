package com.css.modules.lawprojectlistt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 执法事项清单
 *
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
public interface LawProjectListTService extends IService<LawProjectListTEntity> {

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
    void saveLawProjectListT(LawProjectListTEntity announceLawProjectListT);
    /**
     * 修改
     */
    void updateLawProjectListT(LawProjectListTEntity announceLawProjectListT);
    /**
     * 导入excel
     */
    String importLawProjectListT(MultipartFile file) throws Exception;

    /**
     * 获取按执法类别分组的数据
     * @param list 执法事项list
     * @returnl
     */
    List<JSONObject> getLawTypeNameStatistics(List<LawProjectListTEntity> list);
    /**
     * 按执法主体分组的数据
     * @param list 执法事项list
     * @returnl
     */
    List<JSONObject> getLawMainListStatistics(List<LawProjectListTEntity> list);
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    List<LawProjectListTEntity> getProcessedData(List<LawProjectListTEntity> list);
    /**
     * 下载全部
     * @return
     */
    List<LawProjectListTEntity> downloadAll();

    /**
     * 批量下载
     * @return
     */
    List<LawProjectListTEntity> downloadBatch(Map<String, Object> mapIds);

}

