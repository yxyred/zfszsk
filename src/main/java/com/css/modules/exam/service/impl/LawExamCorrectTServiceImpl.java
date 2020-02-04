package com.css.modules.exam.service.impl;

import com.css.common.enums.CommonEnums;
import com.css.common.utils.*;
import com.css.modules.exam.entity.LawExamQuestionTEntity;
import com.css.modules.exam.entity.LawExamUserAnswerTEntity;
import com.css.modules.exam.entity.LawExamUserTEntity;
import com.css.modules.exam.service.*;
import com.css.modules.jgzfkuserinfo.entity.JgZfkUserInfoEntity;
import com.css.modules.jgzfkuserinfo.service.JgZfkUserInfoService;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.css.modules.exam.dao.LawExamCorrectTDao;
import com.css.modules.exam.entity.LawExamCorrectTEntity;


@Service("lawExamCorrectTService")
public class LawExamCorrectTServiceImpl extends ServiceImpl<LawExamCorrectTDao, LawExamCorrectTEntity> implements LawExamCorrectTService {

    @Autowired
    LawExamUserAnswerTService lawExamUserAnswerTService;
    @Autowired
    LawExamTestTService lawExamTestTService;
    @Autowired
    LawExamQuestionTService lawExamQuestionTService;
    @Autowired
    LawExamUserTService lawExamUserTService;
    @Autowired
    private REnumUtil rEnumUtil;
    @Autowired
    private JgZfkUserInfoService jgZfkUserInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String testId = (String)params.get("testName");
        String examTime = (String)params.get("examTime");

        IPage<LawExamCorrectTEntity> page = this.page(
                new Query<LawExamCorrectTEntity>().getPage(params),
                new QueryWrapper<LawExamCorrectTEntity>()
                        .like(StringUtils.isNotBlank(testId),"TEST_ID", testId)
                        .eq(StringUtils.isNotBlank(examTime),"EXAM_TIME", examTime)
                        .ne("DEL_FLAG", "1")
        );

        return new PageUtils(page);
    }
    /**
     * 获取考生答案 案例解析题部分
     * @param correctId 传入试卷批改ID
     */
    @Override
    public Map getSynInfo(String correctId){
        //获取人工判分的所有 题型
        List sysExamQuestionTypeList = lawExamTestTService.getSynExamQuestionTypeList();
        //提取 该套试卷自动判分  题型的 code
        List sysExamQuestionTypeIdsList = new ArrayList();
        for (int i = 0; i < sysExamQuestionTypeList.size(); i++) {
            Map map = (Map)sysExamQuestionTypeList.get(i);
            sysExamQuestionTypeIdsList.add(map.get("code").toString());
        }
        String[] synExamQuestionTypeIds = new String[sysExamQuestionTypeIdsList.size()];
        for (int i = 0; i < sysExamQuestionTypeList.size(); i++) {
            Map map = (Map)sysExamQuestionTypeList.get(i);
            synExamQuestionTypeIds[i] = map.get("code").toString();
        }
        //考生的答案
        List<LawExamUserAnswerTEntity> lawExamUserAnswerTEntityList = lawExamUserAnswerTService.getSynAnswer(correctId, synExamQuestionTypeIds);
        //批改表对象
        LawExamCorrectTEntity lawExamCorrectTEntity = this.getById(correctId);
        //获取试卷详情 的 指定题型的信息 包含答案 用于判分
        //从试卷详情里面 获取 试卷名称
        String testId = lawExamCorrectTEntity.getTestId();
        lawExamCorrectTEntity.setTestId(testId);
        Map testDetail= lawExamTestTService.getInfo(testId,synExamQuestionTypeIds);
        lawExamCorrectTEntity.setTestName(testDetail.get("testName").toString());
        //该试卷 的所有 指定题型 试题 含答案
        List <LawExamQuestionTEntity> LawExamQuestionTList = (List <LawExamQuestionTEntity>)testDetail.get("lawExamDetail");

        List<Map<String,Object>> caseAnalysisList = new ArrayList<>();

        for (int i = 0; i < LawExamQuestionTList.size(); i++) {

            Map<String,Object> caseAnalysis = new HashedMap();
            LawExamQuestionTEntity lawExamQuestionTEntity = LawExamQuestionTList.get(i);
            String id = lawExamQuestionTEntity.getId();
            caseAnalysis.put("subject",lawExamQuestionTEntity.getSubject());
            caseAnalysis.put("problemDescribe",lawExamQuestionTEntity.getProblemDescribe());
            caseAnalysis.put("orderNum",lawExamQuestionTEntity.getOrderNum());
            caseAnalysis.put("answerResult",lawExamQuestionTEntity.getAnswerResult());
            int index = -1;
            boolean has = false;
            for (int j = 0; j < lawExamUserAnswerTEntityList.size(); j++) {
                LawExamUserAnswerTEntity lawExamUserAnswerTEntity = lawExamUserAnswerTEntityList.get(j);
                String questionId = lawExamUserAnswerTEntity.getQuestionId();
                if(id.equals(questionId)){
                    index = j;
                    has = true;
                    break;
                }
            }

            if(index != -1 && has == true){
                caseAnalysis.put("userAnswer",lawExamUserAnswerTEntityList.get(index).getAnswerResult());
            }else {
                caseAnalysis.put("userAnswer","");
            }
            caseAnalysisList.add(caseAnalysis);
        }

        Map<String,Object> result = new HashedMap();
//        result.put("lawExamCorrect",lawExamCorrectTEntity);
//        result.put("lawExamUserAnswerList",lawExamUserAnswerTEntityList);
//        result.put("lawExamUserAnswerList",lawExamUserAnswerTEntityList);

        result.put("correctId",correctId);
        result.put("userName",lawExamCorrectTEntity.getUserName());
        result.put("caseAnalysis",caseAnalysisList);




//        传入试卷批改ID
//        根据试卷批改ID 去 考生答案表 根据题型ID和字典项 获取案例解析题答案

//        根据试卷ID 去 去试卷关联题目表 获取 该套试卷的所有题ID

//        去试题库 按题目ID 找出所有试题

//        根据题型ID 找出 案例解析题

//        返回 题目 题目答案 题目编号 考生答案
        return result;
    }


    /**
     * 人工批改提交
     * @param params
     */
    @Override
    public String synSave(Map<String, Object> params){

        //获取分数 修改批改分数 修改批改状态 修改总分 修改及格状态
        // 修改考生的考试分数 和 及格状态
        String correctId = params.get("id").toString();
        int synScore = Integer.parseInt(params.get("score").toString());
        if(StringUtils.isNotEmpty(correctId)){
            //分数不能为负数
            if(0<=synScore){
                //考试批改表
                LawExamCorrectTEntity lawExamCorrectTEntity = this.getById(correctId);
                if(null != lawExamCorrectTEntity){
                    int sysScore = lawExamCorrectTEntity.getSysScore();
                    int sumScore = sysScore + synScore;
                    lawExamCorrectTEntity.setSynScore(synScore);
                    lawExamCorrectTEntity.setFinalScore(sumScore);
                    lawExamCorrectTEntity.setStatus("1");

                    List list = rEnumUtil.getCodeAndNameList(CommonEnums.LawDict.QUALIFIEDSCORE,CommonEnums.LawRoot.LAWROOT);

                    if ( null != list && 0<list.size()) {
                        Map map = (Map)list.get(0);
                        int qualifiedScore = Integer.parseInt(map.get("name").toString());
                        if(qualifiedScore<=sumScore){
                            lawExamCorrectTEntity.setIsQualified("1");
                            //成绩合格 显示同步信息按钮
                            lawExamCorrectTEntity.setSysncInfoStatus("1");
                        }else{
                            lawExamCorrectTEntity.setIsQualified("0");
                            lawExamCorrectTEntity.setSysncInfoStatus("0");
                        }
                    }else{
                        return "获取考试及格字典项失败！";
                    }
                    //考生信息表
                    String userId = lawExamCorrectTEntity.getUserId();
                    if(StringUtils.isNotEmpty(userId)){
                        LawExamUserTEntity lawExamUserTEntity = lawExamUserTService.getById(userId);
                        if(null != lawExamUserTEntity){
                            String isQualified = lawExamCorrectTEntity.getIsQualified();
                            lawExamUserTEntity.setExamScore(sumScore);
                            lawExamUserTEntity.setIsQualified(isQualified);
                            lawExamUserTService.updateById(lawExamUserTEntity);
                            this.updateById(lawExamCorrectTEntity);
                        }else{
                            return "该考生不在考生信息管理中！";

                        }
                    }else{
                        return "该考生不在考生信息管理中！";
                    }
                }else{
                    return "考试批改ID错误！";
                }
            }else{
                return "分数不能为负数！";
            }
        }else{
            return "考试批改ID错误！";

        }
        return "提交成功！";
    }

    /**
     * 批量同步信息
     * @param mapIds
     */
    @Override
    public String sysncInfoBatch(Map<String, Object> mapIds){
        List<JgZfkUserInfoEntity> jgZfkUserInfoList = new ArrayList();
        List<LawExamCorrectTEntity> lawExamCorrectTEntityList = new ArrayList();

        StringBuffer stringBuffer = new StringBuffer();
        List correctIdList = (List) mapIds.get("ids");
        if(0<correctIdList.size()){
            for (int i = 0; i < correctIdList.size(); i++) {
                //批改表
                LawExamCorrectTEntity lawExamCorrectTEntity = this.getById(correctIdList.get(i).toString());
                if(null != lawExamCorrectTEntity) {
                    //成绩校验是否合格
                    Integer finalScore = lawExamCorrectTEntity.getFinalScore();
                    List list = rEnumUtil.getCodeAndNameList(CommonEnums.LawDict.QUALIFIEDSCORE,CommonEnums.LawRoot.LAWROOT);
                    if ( null != list && 0<list.size()) {
                        Map map = (Map)list.get(0);
                        int qualifiedScore = Integer.parseInt(map.get("name").toString());
                        if(qualifiedScore<=finalScore){
                            String userId = lawExamCorrectTEntity.getUserId();
                            if (StringUtils.isNotEmpty(userId)) {
                                //成绩合格同步信息到执法人员信息表
                                //考生信息表
                                LawExamUserTEntity lawExamUserTEntity = lawExamUserTService.getById(userId);
                                List<JgZfkUserInfoEntity> jgZfkUserInfoEntities = jgZfkUserInfoService
                                        .queryByIdNumber(lawExamUserTEntity.getIdcard());
                                //执法人员信息表
                                if (0 < jgZfkUserInfoEntities.size()) {
                                    jgZfkUserInfoEntities.forEach(jgZfkUserInfoEntity -> {
                                        jgZfkUserInfoEntity.setUnit(lawExamUserTEntity.getPlaceInUnit());
                                        jgZfkUserInfoEntity.setContactTel(lawExamUserTEntity.getContactInfo());
                                        jgZfkUserInfoList.add(jgZfkUserInfoEntity);
                                    });
                                } else {
                                    //身份证为空 或 执法人员表中没有该人
                                    JgZfkUserInfoEntity jgZfkUserInfoEntity = new JgZfkUserInfoEntity();
                                    jgZfkUserInfoEntity.setAgentname(lawExamUserTEntity.getName());
                                    jgZfkUserInfoEntity.setSex(lawExamUserTEntity.getSex());
                                    jgZfkUserInfoEntity.setIdNumber(lawExamUserTEntity.getIdcard());
                                    jgZfkUserInfoEntity.setUnit(lawExamUserTEntity.getPlaceInUnit());
                                    jgZfkUserInfoEntity.setContactTel(lawExamUserTEntity.getContactInfo());
                                    jgZfkUserInfoList.add(jgZfkUserInfoEntity);
                                }
                                lawExamCorrectTEntity.setSysncInfoStatus("2");
                                lawExamCorrectTEntityList.add(lawExamCorrectTEntity);
                            } else {
                                stringBuffer.append(lawExamCorrectTEntity.getUserName() + "考生相关信息获取异常");
                            }
                        }else{
                            stringBuffer.append(lawExamCorrectTEntity.getUserName() + "该考生成绩不合格!,不能同步信息！");
                        }
                    }else{
                        return "获取考试及格字典项失败！";
                    }
                }else{
                    stringBuffer.append("没有第" + (i+1) + "个批改项！");
                }
            };
        }else{
            return "没有该批改项！";
        }
        if(0<jgZfkUserInfoList.size() && 0<lawExamCorrectTEntityList.size()){
            jgZfkUserInfoService.saveOrUpdateBatch(jgZfkUserInfoList);
            this.saveOrUpdateBatch(lawExamCorrectTEntityList);
            stringBuffer.append( "同步成功!");
        }
        return stringBuffer.toString();
    }
    /**
     * 历年考试结果统计
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getExamTimeStrStatistics(List<LawExamCorrectTEntity> list, int yearStart, int yearEnd){
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);
        List<Integer> qualifiedList = new ArrayList<>();
        qualifiedList.add(1);
        qualifiedList.add(0);
        //数据的总数
        Map<String, Long> collectExamTimeSum =  list.stream()
                .filter(lawExamCorrectT -> {
                    String examTimeStr = lawExamCorrectT.getExamTimeStr();
                    String isQualified = lawExamCorrectT.getIsQualified();
                    return StringUtils.isBlank(examTimeStr)||StringUtils.isBlank(isQualified)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawExamCorrectTEntity::getExamTimeStr,Collectors.counting()
                        )

                );
        Map<Object, Map<Object,Long>> collectExamTime =  list.stream()
                .filter(lawExamCorrectT -> {
                    String examTimeStr = lawExamCorrectT.getExamTimeStr();
                    String isQualified = lawExamCorrectT.getIsQualified();
                    return StringUtils.isBlank(examTimeStr)||StringUtils.isBlank(isQualified)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawExamCorrectTEntity::getExamTimeStr,Collectors.
                                        groupingBy(LawExamCorrectTEntity::getIsQualified,Collectors.counting())
                        )
                );
        List<JSONObject> jsonObjectExamTimeList = StatisticsUtils.getExamTimetWithQualifiedAndYears(collectExamTimeSum,collectExamTime,qualifiedList,yearsList);
        return jsonObjectExamTimeList;
    }
    /**
     * 按试卷名称 考试结果统计
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getTestNameStrStatistics(List<LawExamCorrectTEntity> list, int yearStart, int yearEnd){
        List<Integer> qualifiedList = new ArrayList<>();
        qualifiedList.add(0);
        qualifiedList.add(1);
        //数据的总数
        Map<String, Long> collectTestNameSum =  list.stream()
                .filter(lawExamCorrectT -> {
                    String testName = lawExamCorrectT.getTestName();
                    String isQualified = lawExamCorrectT.getIsQualified();
                    return StringUtils.isBlank(testName)||StringUtils.isBlank(isQualified)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawExamCorrectTEntity::getTestName,Collectors.counting()
                        )

                );
        Map<Object, Map<Object,Long>> collectTestName =  list.stream()
                .filter(lawExamCorrectT -> {
                    String testName = lawExamCorrectT.getTestName();
                    String isQualified = lawExamCorrectT.getIsQualified();
                    return StringUtils.isBlank(testName)||StringUtils.isBlank(isQualified)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawExamCorrectTEntity::getTestName,Collectors.
                                        groupingBy(LawExamCorrectTEntity::getIsQualified,Collectors.counting())
                        )
                );
        List<JSONObject> jsonObjectTestNameList = StatisticsUtils.getExamTimetWithQualified(collectTestNameSum,collectTestName,qualifiedList);
        return jsonObjectTestNameList;
    }
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    @Override
    public List<LawExamCorrectTEntity> getProcessedData(List<LawExamCorrectTEntity> list){
        list.forEach(lawExamCorrect->{
            //将执法结果时间 格式化到年 放入 examTestName
            String examTimeStr = DateUtils.format(lawExamCorrect.getExamTime(),"yyyy");
            lawExamCorrect.setExamTimeStr(examTimeStr);
        });
        return list;
    }
}