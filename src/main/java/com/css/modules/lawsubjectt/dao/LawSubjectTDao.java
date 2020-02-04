package com.css.modules.lawsubjectt.dao;

import com.css.modules.lawsubjectt.entity.LawSubjectTEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 市场主体名录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-11-18 14:26:25
 */
@Mapper
public interface LawSubjectTDao extends BaseMapper<LawSubjectTEntity> {
    /**
     * 公用批量修改删除状态方法
     */
    int commonUpdateDelFlagBatch(Map<String, Object> map);
    /**
     * 公用全部修改删除状态方法
     */
    int commonUpdateDelFlagAll(String delFlag);
    /**
     * 公用批量修改发布状态方法
     */
    int commonUpdatePubStatusBatch(Map<String, Object> map);
    /**
     * 公用全部修改发布状态方法
     */
    int commonUpdatePubStatusAll(String pubStatus);
}
