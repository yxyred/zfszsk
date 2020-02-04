package com.css.modules.exam.dao;

import com.css.modules.exam.entity.LawExamPlanTEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 考试计划表
 * 
 * @author 
 * @email 
 * @date 2019-11-26 11:12:19
 */
@Mapper
public interface LawExamPlanTDao extends BaseMapper<LawExamPlanTEntity> {
    /**
     * 公用批量修改删除状态方法
     */
    int commonUpdateDelFlagBatch(Map<String, Object> map);
    /**
     * 公用全部修改删除状态方法
     */
    int commonUpdateDelFlagAll(String delFlag);
}
