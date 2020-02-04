package com.css.modules.exam.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamListTDao;
import com.css.modules.exam.entity.LawExamListTEntity;
import com.css.modules.exam.service.LawExamListTService;


@Service("lawExamListTService")
public class LawExamListTServiceImpl extends ServiceImpl<LawExamListTDao, LawExamListTEntity> implements LawExamListTService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LawExamListTEntity> page = this.page(
                new Query<LawExamListTEntity>().getPage(params),
                new QueryWrapper<LawExamListTEntity>()
        );

        return new PageUtils(page);
    }

}