package com.css.modules.exam.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamQuestionTypeTDao;
import com.css.modules.exam.entity.LawExamQuestionTypeTEntity;
import com.css.modules.exam.service.LawExamQuestionTypeTService;


@Service("lawExamQuestionTypeTService")
public class LawExamQuestionTypeTServiceImpl extends ServiceImpl<LawExamQuestionTypeTDao, LawExamQuestionTypeTEntity> implements LawExamQuestionTypeTService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LawExamQuestionTypeTEntity> page = this.page(
                new Query<LawExamQuestionTypeTEntity>().getPage(params),
                new QueryWrapper<LawExamQuestionTypeTEntity>()
        );

        return new PageUtils(page);
    }

}