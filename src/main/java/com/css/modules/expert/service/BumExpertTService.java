package com.css.modules.expert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.expert.entity.BumExpertAndExperienceTEntity;
import com.css.modules.expert.entity.BumExpertTEntity;
import com.css.modules.expert.entity.BumExpertTExcelEntity;
import com.css.modules.jgzfkuserinfo.entity.JgZfkUserInfoEntity;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 专家库
 *
 * @author liukai
 * @date 2019-11-12 17:17:24
 */
public interface BumExpertTService extends IService<BumExpertTEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean saveExpert(BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity);

    BumExpertAndExperienceTEntity selectById(String id);

    boolean updateById(BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity);

    void importBumExpertT(MultipartFile file);

    List<BumExpertTExcelEntity> exportByIds(Map<String, Object> map);
    /**
     * 获取按技术专长分组的数据
     * @param list 执法事项list
     * @returnl
     */
    List<JSONObject> getExpertiseStatistics(List<BumExpertTEntity> list);
    /**
     * 获取按工作单位类别名称分组的数据
     * @param list 执法事项list
     * @returnl
     */
    List<JSONObject> getUnitTypeNameStatistics(List<BumExpertTEntity> list);
    /**
     * 获取按省份名称分组的数据
     * @param list 执法事项list
     * @returnl
     */
    List<JSONObject> getProvinceStatistics(List<BumExpertTEntity> list);

    PageUtils randomExtraction(Map<String, Object> params);

    PageUtils selectPage(Map<String, Object> params, int nums);
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    List<BumExpertTEntity> getProcessedData(List<BumExpertTEntity> list);
}

