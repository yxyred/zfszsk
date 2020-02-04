package com.css.modules.jgzskcase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.css.modules.jgzskcase.entity.JgZskCaseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 执法案例库
 * 
 * @author
 * @email
 * @date 2019-11-13 15:59:02
 */
@Mapper
public interface JgZskCaseDao extends BaseMapper<JgZskCaseEntity> {
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
    int commonUpdateTypicalCaseBatch(Map<String, Object> map);
}
