package com.css.modules.exam.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamAnswerTDao;
import com.css.modules.exam.entity.LawExamAnswerTEntity;
import com.css.modules.exam.service.LawExamAnswerTService;


@Service("lawExamAnswerTService")
public class LawExamAnswerTServiceImpl extends ServiceImpl<LawExamAnswerTDao, LawExamAnswerTEntity> implements LawExamAnswerTService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LawExamAnswerTEntity> page = this.page(
                new Query<LawExamAnswerTEntity>().getPage(params),
                new QueryWrapper<LawExamAnswerTEntity>()
        );

        return new PageUtils(page);
    }

}