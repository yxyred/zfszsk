package com.css.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.enums.CommonEnums;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;
import com.css.common.utils.REnumUtil;
import com.css.common.utils.RandomUtils;
import com.css.modules.exam.dao.LawExamTestSimpleTDao;
import com.css.modules.exam.entity.LawExamQuestionTEntity;
import com.css.modules.exam.entity.LawExamRelateTEntity;
import com.css.modules.exam.entity.LawExamTestSimpleTEntity;
import com.css.modules.exam.entity.LawExamTestSimpleTEntity;
import com.css.modules.exam.service.LawExamQuestionTService;
import com.css.modules.exam.service.LawExamRelateTService;
import com.css.modules.exam.service.LawExamTestSimpleTService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("lawExamTestSimpleTService")
public class LawExamTestSimpleTServiceImpl extends ServiceImpl<LawExamTestSimpleTDao, LawExamTestSimpleTEntity> implements LawExamTestSimpleTService {
    /**
     * 获取考试列表 name和id
     * @return
     */
    @Override
    public List<LawExamTestSimpleTEntity> simpleListAll(){

        List<LawExamTestSimpleTEntity> list = this.list(new QueryWrapper<LawExamTestSimpleTEntity>()
                .select("ID","NAME")
                .orderByAsc("CREATE_TIME")
                .ne("DEL_FLAG", "1"));
        return list;
    }

    /**
     * 获取考试列表 name和id
     * 不返回 已经使用的
     * @return
     */
    @Override
    public List<LawExamTestSimpleTEntity> simpleListAllWithoutUsed(){

        List<LawExamTestSimpleTEntity> list = this.list(new QueryWrapper<LawExamTestSimpleTEntity>()
                .select("ID","NAME")
                .orderByAsc("CREATE_TIME")
                .ne("STATUS", "1")
                .ne("DEL_FLAG", "1"));
        return list;
    }

}