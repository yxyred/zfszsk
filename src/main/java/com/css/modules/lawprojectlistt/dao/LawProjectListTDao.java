package com.css.modules.lawprojectlistt.dao;

import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 执法事项清单
 * 
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
@Mapper
public interface LawProjectListTDao extends BaseMapper<LawProjectListTEntity> {
    /**
     * 公用批量修改发布状态方法
     */
    int commonUpdatePubStatusBatch(Map<String, Object> map);
    /**
     * 公用全部修改发布状态方法
     */
    int commonUpdatePubStatusAll(String pubStatus);
    /**
     * 公用批量修改删除状态方法
     */
    int commonUpdateDelFlagBatch(Map<String, Object> map);
    /**
     * 公用全部修改删除状态方法
     */
    int commonUpdateDelFlagAll(String delFlag);
}
