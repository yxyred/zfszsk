package com.css.modules.exam.service.impl;

import com.css.common.enums.CommonEnums;
import com.css.common.utils.REnumUtil;
import com.css.common.utils.StringOrderUtil;
import com.css.modules.exam.entity.*;
import com.css.modules.exam.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamUserAnswerTDao;


@Service("lawExamUserAnswerTService")
public class LawExamUserAnswerTServiceImpl extends ServiceImpl<LawExamUserAnswerTDao, LawExamUserAnswerTEntity> implements LawExamUserAnswerTService {

    @Autowired
    LawExamTestTService lawExamTestTService;
    @Autowired
    LawExamQuestionTService lawExamQuestionTService;
    @Autowired
    LawExamUserTService lawExamUserTService;
    @Autowired
    private LawExamCorrectTService lawExamCorrectTService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LawExamUserAnswerTEntity> page = this.page(
                new Query<LawExamUserAnswerTEntity>().getPage(params),
                new QueryWrapper<LawExamUserAnswerTEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存考生答案
     * @param lawExamUserAnswerTList
     * @return
     */
    @Override
    public String saveUserAnswer(List<LawExamUserAnswerTEntity> lawExamUserAnswerTList){
        //考生答案表 存储 考试批次ID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //批改表对象
        LawExamCorrectTEntity lawExamCorrectTEntity = new LawExamCorrectTEntity();
        lawExamCorrectTEntity.setId(uuid);
        lawExamUserAnswerTList.forEach(lawExamUserAnswerTEntity -> lawExamUserAnswerTEntity.setCorrectId(uuid));
//        this.saveBatch(lawExamUserAnswerTList);
        //存储考生试卷的 同时 往 批改表中 存储 下面对应的信息
        //考生答案对象
        LawExamUserAnswerTEntity lawExamUserAnswerT = lawExamUserAnswerTList.get(0);

        //根据考生ID 和 试卷ID 去 考生信息库 获取 考生姓名 考试姓名 考试日期 考试开始时间 考试结束时间
        //案例解析题名称 存 考试姓名+案例解部分
        //TODO
        String userId = lawExamUserAnswerT.getUserId();
        LawExamUserTEntity lawExamUserTEntity = lawExamUserTService.getById(userId);
        lawExamCorrectTEntity.setUserId(userId);
        lawExamCorrectTEntity.setUserName(lawExamUserTEntity.getName());
        lawExamCorrectTEntity.setExamTime(lawExamUserTEntity.getExamTime());
        lawExamCorrectTEntity.setExamStartTime(lawExamUserTEntity.getExamStartTime());
        lawExamCorrectTEntity.setExamEndTime(lawExamUserTEntity.getExamEndTime());
        lawExamCorrectTEntity.setCaseAnalysisName(lawExamUserTEntity.getName()+"案例解部分");
        //批改表 存 考生答案表的ID

        //获取自动判分的所有 题型
        List sysExamQuestionTypeList = lawExamTestTService.getSysExamQuestionTypeList();
        //提取 该套试卷自动判分  题型的 code
        List sysExamQuestionTypeIdsList = new ArrayList();
        for (int i = 0; i < sysExamQuestionTypeList.size(); i++) {
            Map map = (Map)sysExamQuestionTypeList.get(i);
            sysExamQuestionTypeIdsList.add(map.get("code").toString());
        }
        String[] sysExamQuestionTypeIds = new String[sysExamQuestionTypeIdsList.size()];
        for (int i = 0; i < sysExamQuestionTypeList.size(); i++) {
            Map map = (Map)sysExamQuestionTypeList.get(i);
            sysExamQuestionTypeIds[i] = map.get("code").toString();
        }


        //获取试卷详情 的 指定题型的信息 包含答案 用于判分
        //从试卷详情里面 获取 试卷名称
        String testId = lawExamUserAnswerT.getTestId();
        lawExamCorrectTEntity.setTestId(testId);
        Map testDetail= lawExamTestTService.getInfo(testId,sysExamQuestionTypeIds);
        lawExamCorrectTEntity.setTestName(testDetail.get("testName").toString());
        //该试卷 的所有 指定题型 试题 含答案
        List <LawExamQuestionTEntity> LawExamQuestionTList = (List <LawExamQuestionTEntity>)testDetail.get("lawExamDetail");
        int sumScore = 0;
        for (int j = 0; j < LawExamQuestionTList.size(); j++) {
            //试卷题目对象
            LawExamQuestionTEntity lawExamQuestionTEntity = LawExamQuestionTList.get(j);
            String questionTypeId = lawExamQuestionTEntity.getQuestionTypeId();
            String score = lawExamQuestionTEntity.getExamScore();
            String questionId =  lawExamQuestionTEntity.getId();
            //自动判分的题
            if(sysExamQuestionTypeIdsList.contains(questionTypeId)){
                //判分
                for (int k = 0; k < lawExamUserAnswerTList.size(); k++) {
                    //该套试卷 题目的id 和 考生答案表中 题目id相同 判分
                    if(questionId.equals(lawExamUserAnswerTList.get(k).getQuestionId())){
                       String userAnswerResult = lawExamUserAnswerTList.get(k).getAnswerResult();
                       String examAnswerResult =  lawExamQuestionTEntity.getAnswerResult();
                       boolean result = StringOrderUtil.isScrambledString(userAnswerResult,examAnswerResult);
                       if(result){
                           sumScore = sumScore + Integer.parseInt(score);
                           lawExamUserAnswerTList.get(k).setAnswerScore(Integer.parseInt(score));
                       }
                    }
                }
            }
        }
        lawExamCorrectTEntity.setSysScore(sumScore);
        lawExamCorrectTService.save(lawExamCorrectTEntity);

        this.saveBatch(lawExamUserAnswerTList);
        //获取自动判分的题型的所有试题的答案

        //对自动判分的题型的所有试题 系统自动判分
        //其他题型 只存储 考生答案的ID


        //根据题目ID 去 题库  获取 该题答案

        return "提交试卷成功";
    }

    /**
     * 根据考试批次ID 和题型ID 获取 答案
     * @param correctId
     * @param examQuestionTypeId
     * @return
     */
    @Override
    public List<LawExamUserAnswerTEntity> getSynAnswer(String correctId,String ...examQuestionTypeId){
        List<LawExamUserAnswerTEntity> lawExamUserAnswerTEntityList =  new ArrayList<>();
        if(null != examQuestionTypeId && 0 < examQuestionTypeId.length){
            if(StringUtils.isNotBlank(correctId)){
                lawExamUserAnswerTEntityList = this.list(new QueryWrapper<LawExamUserAnswerTEntity>()
                        .eq("CORRECT_ID",correctId)
                        .in("QUESTION_TYPE_ID",examQuestionTypeId)
                        .ne("DEL_FLAG","1")
                );
            }
        }

        return  lawExamUserAnswerTEntityList;

    }









}