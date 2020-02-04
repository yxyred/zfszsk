package com.css.modules.expert.service.impl;

import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;
import com.css.modules.expert.dao.BumExpertExperienceTDao;
import com.css.modules.expert.entity.BumExpertExperienceTEntity;
import com.css.modules.expert.service.BumExpertExperienceTService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("bumExpertExperienceTService")
public class BumExpertExperienceTServiceImpl extends ServiceImpl<BumExpertExperienceTDao, BumExpertExperienceTEntity> implements BumExpertExperienceTService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BumExpertExperienceTEntity> page = this.page(
                new Query<BumExpertExperienceTEntity>().getPage(params),
                new QueryWrapper<BumExpertExperienceTEntity>()
        );

        return new PageUtils(page);
    }

}