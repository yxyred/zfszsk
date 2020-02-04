

package com.css.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.css.modules.sys.entity.SysLogTEntity;
import com.css.common.utils.PageUtils;

import java.util.Map;


/**
 * 系统日志
 */
public interface SysLogService extends IService<SysLogTEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
