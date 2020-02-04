package com.css.modules.expert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.common.utils.PageUtils;
import com.css.modules.expert.entity.BumExpertExperienceTEntity;

import java.util.Map;

/**
 * 专家辅法经历表
 *
 * @author liukai
 * @date 2019-11-18 14:44:19
 */
public interface BumExpertExperienceTService extends IService<BumExpertExperienceTEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

