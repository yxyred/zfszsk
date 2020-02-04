package com.css.modules.expert.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.css.modules.expert.entity.BumExpertTEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.css.modules.expert.entity.BumExpertTExcelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 专家库
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-11-12 17:17:24
 */
@Mapper
public interface BumExpertTDao extends BaseMapper<BumExpertTEntity> {

    List<BumExpertTExcelEntity> exportByIds(Map<String, Object> map);


    IPage<BumExpertTExcelEntity> selectMyPage(IPage<BumExpertTExcelEntity> page, @Param("ew") Wrapper<BumExpertTExcelEntity> queryWrapper);

}
