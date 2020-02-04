package com.css.modules.exam.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamRelateTDao;
import com.css.modules.exam.entity.LawExamRelateTEntity;
import com.css.modules.exam.service.LawExamRelateTService;


@Service("lawExamRelateTService")
public class LawExamRelateTServiceImpl extends ServiceImpl<LawExamRelateTDao, LawExamRelateTEntity> implements LawExamRelateTService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LawExamRelateTEntity> page = this.page(
                new Query<LawExamRelateTEntity>().getPage(params),
                new QueryWrapper<LawExamRelateTEntity>()
        );

        return new PageUtils(page);
    }
    /**
     * 根据试卷 获取该试卷 与题库 对应的 所有题的ID
     * @param testId
     * @return
     */
    @Override
    public List getQuestionIds(String testId){
        List questionIds = new ArrayList();
        if (StringUtils.isNotBlank(testId)){
            List <LawExamRelateTEntity> lawExamRelateTList  = this.list(new QueryWrapper<LawExamRelateTEntity>()
                    .select("ID","TEST_ID","QUESTION_ID","ORDER_NUM","QUESTION_NUM")
                    .eq(StringUtils.isNotBlank(testId),"TEST_ID", testId)
                    .orderByAsc("ORDER_NUM")
                    .ne("DEL_FLAG", "1"));
            lawExamRelateTList.forEach(lawExamRelateTEntity -> questionIds.add(lawExamRelateTEntity.getQuestionId()));
        }
        return questionIds;
    }

    /**
     * 根据试卷 获取该试卷 与题库 对应的 所有题的ID和题目编号 和顺序号
     * @param testId
     * @return
     */
    @Override
    public List getQuestionIdsAndQuestionNum(String testId){
        List list = new ArrayList();
        if (StringUtils.isNotBlank(testId)){
            List <LawExamRelateTEntity> lawExamRelateTList  = this.list(new QueryWrapper<LawExamRelateTEntity>()
                    .select("ID","TEST_ID","QUESTION_ID","ORDER_NUM","QUESTION_NUM")
                    .eq(StringUtils.isNotBlank(testId),"TEST_ID", testId)
                    .orderByAsc("ORDER_NUM")
                    .ne("DEL_FLAG", "1"));
            lawExamRelateTList.forEach(lawExamRelateTEntity -> {
                Map map = new HashedMap();
                map.put("questionId",lawExamRelateTEntity.getQuestionId());
                map.put("questionNum",lawExamRelateTEntity.getQuestionNum());
                map.put("orderNum",lawExamRelateTEntity.getOrderNum());
                list.add(map);
            });
        }
        return list;
    }

}