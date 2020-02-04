

package com.css.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * 文件上传
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
