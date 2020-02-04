

package com.css.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.css.modules.sys.entity.SysLogTEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 */
@Mapper
public interface SysLogDao extends BaseMapper<SysLogTEntity> {
	
}
