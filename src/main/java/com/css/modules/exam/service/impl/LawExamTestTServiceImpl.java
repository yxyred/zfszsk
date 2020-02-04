package com.css.modules.exam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.css.common.enums.CommonEnums;
import com.css.common.utils.*;
import com.css.modules.exam.entity.*;
import com.css.modules.exam.service.*;
import com.css.modules.sys.entity.SysUserTokenEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.css.modules.exam.dao.LawExamTestTDao;


@Service("lawExamTestTService")
public class LawExamTestTServiceImpl extends ServiceImpl<LawExamTestTDao, LawExamTestTEntity> implements LawExamTestTService {
    @Autowired
    private REnumUtil rEnumUtil;
    @Autowired
    private RandomUtils randomUtils;
    @Autowired
    private LawExamQuestionTService lawExamQuestionTService;
    @Autowired
    LawExamRelateTService lawExamRelateTService;
    @Autowired
    LawExamUserTService lawExamUserTService;
    @Autowired
    LawExamPlanTService lawExamPlanTService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");
        String category = (String)params.get("category");
        String status = (String)params.get("status");

        IPage<LawExamTestTEntity> page = this.page(
                new Query<LawExamTestTEntity>().getPage(params),
                new QueryWrapper<LawExamTestTEntity>()
                        .like(StringUtils.isNotBlank(name),"NAME", name)
                        .like(StringUtils.isNotBlank(category),"CATEGORY", category)
                        .eq(StringUtils.isNotBlank(status),"STATUS", status)
                        .ne("DEL_FLAG", "1")

        );
        return new PageUtils(page);
    }
    /**
     * 考生参加的考试列表
     * @param params
     * @return
     */
    @Override
    public PageUtils examTestQueryPage(Map<String, Object> params) {

        /*SysUserTokenEntity userEntity = ShiroUtils.getUserEntity();
        if(null == userEntity){
            return null;
        }
        //身份证号不能为空
        if(StringUtils.isBlank(userEntity.getIdCardNo())){
            return null;
        }
        LawExamUserTEntity lawExamUserTEntity = lawExamUserTService.getOne(
                new QueryWrapper<LawExamUserTEntity>()
                        .lambda()
                        .eq(LawExamUserTEntity::getIdcard, userEntity.getIdCardNo())
        );
        if(null == lawExamUserTEntity){
            return null;
        }*/
        SysUserTokenEntity userEntity = new SysUserTokenEntity();
        userEntity.setIdCardNo("123456789012345678");


        //根据用户身份证号 在考生信息表中 查询该用户参加的考试计划
        String testName = (String)params.get("testName");
        String examTime = (String)params.get("examTime");

        List<LawExamUserTEntity> lawExamUserTEntitys = lawExamUserTService.list(new QueryWrapper<LawExamUserTEntity>()
                .like(StringUtils.isNotBlank(testName),"TEST_NAME", testName)
                .eq("IDCARD", userEntity.getIdCardNo())
                .eq(StringUtils.isNotBlank(examTime),"EXAM_TIME", examTime)
                .ne("DEL_FLAG", "1")
        );

        //判断时间结束状态

        //根据考试日期 和 具体时间 改变返回前段的考试状态 并 将状态在数据库中存储
        List lawExamPlanIds = new ArrayList();
        for (int i = 0; i < lawExamUserTEntitys.size(); i++) {
            if(StringUtils.isNotBlank(lawExamUserTEntitys.get(i).getPlanId())){
                lawExamPlanIds.add(lawExamUserTEntitys.get(i).getPlanId());
            }
        }

        IPage page = new Page<>();
        if(0<lawExamPlanIds.size()){
            page = lawExamPlanTService.page(
                    new Query<LawExamPlanTEntity>().getPage(params),
                    new QueryWrapper<LawExamPlanTEntity>()
                            .in("ID", lawExamPlanIds)
                            .ne("DEL_FLAG", "1")

            );
            if(null != page.getRecords()){
                List<LawExamPlanTEntity> lawExamPlanTEntities = page.getRecords();
                //根据状态 修改该表数据库中的状态
                lawExamPlanTService.updateExamPlanStatus(lawExamPlanTEntities);
                //返回 前端数据 试卷ID 试卷名称 试卷时间 答题时长 考试状态
                List<NetLawExamPlanTEntity> netLawExamPlanTEntities = new ArrayList<>();
                lawExamPlanTEntities.forEach(lawExamPlanTEntity -> {
                    NetLawExamPlanTEntity netLawExamPlanTEntity = new NetLawExamPlanTEntity();
                    netLawExamPlanTEntity.setTestId(lawExamPlanTEntity.getTestId());
                    netLawExamPlanTEntity.setTestName(lawExamPlanTEntity.getTestName());
                    netLawExamPlanTEntity.setExamTime(lawExamPlanTEntity.getExamTime());
                    netLawExamPlanTEntity.setExamStartTime(lawExamPlanTEntity.getExamStartTime());
                    netLawExamPlanTEntity.setExamEndTime(lawExamPlanTEntity.getExamEndTime());
                    netLawExamPlanTEntity.setLimitTime(lawExamPlanTEntity.getLimitTime());
                    netLawExamPlanTEntity.setStatus(lawExamPlanTEntity.getStatus());
                    netLawExamPlanTEntities.add(netLawExamPlanTEntity);
                });
                page.setRecords(netLawExamPlanTEntities);

            }
        }

        return new PageUtils(page);



    }


    /**
     * 生成试卷
     */
    @Override
    public String saveLawExamTestT(LawExamTestTEntity lawExamTestT){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        lawExamTestT.setId(uuid);
        lawExamTestT.setStatus("0");
        lawExamTestT.setDelFlag("0");
        lawExamTestT.setTestCreateTime(new Date());
        if (null != lawExamTestT.getQuestion() && 0 < lawExamTestT.getQuestion().size()) {
            int orderNum = 1;
            for (int i = 0; i < lawExamTestT.getQuestion().size() ; i++) {
                //前段传来 需要生成的 题型 数量 总分
                Map question = (Map)lawExamTestT.getQuestion().get(i);
                if(StringUtils.isBlank(question.get("questionTypeId").toString())){
                    return "题型不能为空！";
                }
                if(StringUtils.isBlank(question.get("num").toString())){
                    return "数量不能为空！";
                }
                if(StringUtils.isBlank(question.get("score").toString())){
                    return "分数不能为空！";
                }
                String questionTypeId =  question.get("questionTypeId").toString();
                int num = Integer.parseInt(question.get("num").toString());
                int score = Integer.parseInt(question.get("score").toString());

                //获取题库中该题型的所有题
                List <LawExamQuestionTEntity> LawExamQuestionTList  = lawExamQuestionTService.list(new QueryWrapper<LawExamQuestionTEntity>()
                        .select("ID","QUESTION_TYPE_ID")
                        .eq(StringUtils.isNotBlank(questionTypeId),"QUESTION_TYPE_ID", questionTypeId)
                        .ne("DEL_FLAG", "1"));
                //随机生成 位置
                int randomArray[] =  randomUtils.randomArray(0,LawExamQuestionTList.size()-1,num);
                //试卷与题库的关系 即 该套试卷的题目
                List <LawExamRelateTEntity> lawExamRelateTList = new ArrayList <LawExamRelateTEntity>();
                for (int j = 0; j < randomArray.length; j++) {
                    LawExamRelateTEntity lawExamRelateTEntity = new LawExamRelateTEntity();
                    String lawExamTestId = LawExamQuestionTList.get(randomArray[j]).getId();
                    lawExamRelateTEntity.setQuestionId(lawExamTestId);
                    lawExamRelateTEntity.setTestId(lawExamTestT.getId());
                    lawExamRelateTEntity.setOrderNum(orderNum);
                    //题目编号
                    lawExamRelateTEntity.setQuestionNum(j+1);
                    lawExamRelateTEntity.setDelFlag("0");
                    lawExamRelateTList.add(lawExamRelateTEntity);
                    orderNum++;
                }
                lawExamRelateTService.saveBatch(lawExamRelateTList);
            }
        }
        this.save(lawExamTestT);
        return "保存成功！";
    }
    /**
     * 获取服务器当前时间与考试结束时间的差值
     * @param testId
     * @return
     */
    @Override
    public long calculateTestTimeGapSecond(String testId){
        if(StringUtils.isNotBlank(testId)){
            List<LawExamPlanTEntity> lawExamPlanlist = lawExamPlanTService.list(new QueryWrapper<LawExamPlanTEntity>()
                    .lambda()
                    .select(LawExamPlanTEntity::getExamTime, LawExamPlanTEntity::getExamEndTime)
                    .eq(LawExamPlanTEntity::getTestId, testId)
                    .ne(LawExamPlanTEntity::getDelFlag, "1")
            );
            if(null != lawExamPlanlist && 0<lawExamPlanlist.size()){
                LawExamPlanTEntity lawExamPlanTEntity = lawExamPlanlist.get(0);
                Date examTime = lawExamPlanTEntity.getExamTime();
                Date examEndTime = lawExamPlanTEntity.getExamEndTime();

                if(null != examTime && null != examEndTime){
                    String day = DateUtils.format(examTime, "yyyy-MM-dd");
                    String time = DateUtils.format(examEndTime, "HH:mm:ss");
                    String dayTime = day + " "+ time;
                    String currentTime = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
                    long second = DateUtils.calculateTimeGapSecond(currentTime, dayTime);
                    return second;
                }else{
                    return -1;
                }
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    /**
     * 获取试卷详情 的 全部题型信息 不包含答案 用于考生考试
     * @param testId 试卷ID
     */
    @Override
    public Map getInfo(String testId){

        //获取试卷信息
        LawExamTestTEntity lawExamTestTEntity = this.getById(testId);
        Map resultMap = new HashMap();
        resultMap.put("testName",lawExamTestTEntity.getName());
        resultMap.put("limitTime",lawExamTestTEntity.getLimitTime());
        //获取该试卷 与题库 对应的 所有题的ID
        List questionIds  = lawExamRelateTService.getQuestionIds(testId);
        //根据 所有题的ID 获取题库中 该试卷 对应的 所有题
        List <LawExamQuestionTEntity> LawExamQuestionTList  = new ArrayList<>();
        if (null != questionIds && 0 < questionIds.size()) {
            LawExamQuestionTList  = lawExamQuestionTService.list(new QueryWrapper<LawExamQuestionTEntity>()
                    .lambda()
                    .select(LawExamQuestionTEntity::getId,
                            LawExamQuestionTEntity::getQuestionTypeId,
                            LawExamQuestionTEntity::getSubject,
                            LawExamQuestionTEntity::getProblemDescribe
                    )
                    .in(LawExamQuestionTEntity::getId,questionIds)
                    .ne(LawExamQuestionTEntity::getDelFlag, "1"));
            LawExamQuestionTList = this.addQusetionNum(LawExamQuestionTList,testId);
            LawExamQuestionTList = this.getDataByQuestionType(LawExamQuestionTList);
        }
        resultMap.put("lawExamDetail",LawExamQuestionTList);
        return resultMap;
    }

    /**
     * 获取试卷详情 的 指定题型的信息 包含答案 用于判分
     * @param testId 试卷ID
     * @param examQuestionTypeId 试题类型code
     */
    @Override
    public Map getInfo(String testId,String ...examQuestionTypeId){

        //获取试卷信息
        LawExamTestTEntity lawExamTestTEntity = this.getById(testId);
        Map resultMap = new HashMap();
        resultMap.put("testName",lawExamTestTEntity.getName());
        resultMap.put("limitTime",lawExamTestTEntity.getLimitTime());
        //获取该试卷 与题库 对应的 所有题的ID
        List questionIds  = lawExamRelateTService.getQuestionIds(testId);
        //根据 所有题的ID 获取题库中 该试卷 对应的 所有题
        List <LawExamQuestionTEntity> LawExamQuestionTList  = new ArrayList<>();
        if(null != examQuestionTypeId && 0 < examQuestionTypeId.length){
            if (null != questionIds && 0 < questionIds.size()) {
                LawExamQuestionTList  = lawExamQuestionTService.list(new QueryWrapper<LawExamQuestionTEntity>()
                        .select("ID","QUESTION_TYPE_ID","SUBJECT","PROBLEM_DESCRIBE","ANSWER_RESULT")
                        .in("QUESTION_TYPE_ID",examQuestionTypeId)
                        .in("ID",questionIds)
                        .ne("DEL_FLAG", "1"));
                LawExamQuestionTList = this.addQusetionNum(LawExamQuestionTList,testId);
                LawExamQuestionTList = this.getDataByQuestionType(LawExamQuestionTList);
            }
        }
        resultMap.put("lawExamDetail",LawExamQuestionTList);
        return resultMap;
    }

    /**
     * 加 题目编号和顺序号
     * @param lawExamQuestionTList
     * @param testId
     * @return
     */
    private List <LawExamQuestionTEntity> addQusetionNum(List <LawExamQuestionTEntity> lawExamQuestionTList,String testId){
        List questionIdsAndQuestionNum = lawExamRelateTService.getQuestionIdsAndQuestionNum(testId);
        List <LawExamQuestionTEntity> newLawExamQuestionTList = new ArrayList<>();
        for (int i = 0; i < questionIdsAndQuestionNum.size() ; i++) {
            Map map = (Map)questionIdsAndQuestionNum.get(i);
            int index = -1;
            boolean has = false;
            for (int j = 0; j < lawExamQuestionTList.size() ; j++) {
                if(StringUtils.isNotBlank(map.get("questionId").toString())
                        && map.get("questionId").toString().equals(lawExamQuestionTList.get(j).getId())){
                    index = j;
                    has = true;
                    break;
                }
            }
            if(index != -1 && has == true){
                lawExamQuestionTList.get(index).setQuestionNum(Integer.parseInt(map.get("questionNum").toString()));
                lawExamQuestionTList.get(index).setOrderNum(Integer.parseInt(map.get("orderNum").toString()));
                newLawExamQuestionTList.add(lawExamQuestionTList.get(index));
            }
        }
        return newLawExamQuestionTList;
    }


    /**
     * 传入试题的list 根据试题类型 添加 试题的分数和题型名称  信息
     * @param lawExamQuestionTList 试题的list
     * @return
     */
    @Override
    public List <LawExamQuestionTEntity> getDataByQuestionType(List <LawExamQuestionTEntity> lawExamQuestionTList){
        //题目类型
        List examQuestionType = rEnumUtil.getExamQuestionTypeList(
                CommonEnums.LawDict.EXAMQUESTIONTYPE,CommonEnums.LawRoot.LAWROOT);
        //匹配分数 和 题型名称
        for (int i = 0; i < lawExamQuestionTList.size() ; i++) {
            int index = -1;
            boolean has = false;
            for (int j = 0; j < examQuestionType.size() ; j++) {
                Map map = (Map)examQuestionType.get(j);
                if(StringUtils.isNotBlank(lawExamQuestionTList.get(i).getQuestionTypeId())
                        && map.get("code").toString().equals(lawExamQuestionTList.get(i).getQuestionTypeId())){
                    index = j;
                    has = true;
                    break;
                }
            }
            if(index != -1 && has == true){
                Map map = (Map)examQuestionType.get(index);
                lawExamQuestionTList.get(i).setExamScore(map.get("examScore").toString());
                lawExamQuestionTList.get(i).setQuestionTypeName(map.get("name").toString());
            }
        }
        return lawExamQuestionTList;
    }

    /**
     * 获取自动判分的题型的信息
     * name(题型名称) code(题型code) examScore(分值) sysFlag(是否自动判分的标识)
     *
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    @Override
    public List getSysExamQuestionTypeList(){
        //题目类型
        List examQuestionType = rEnumUtil.getExamQuestionTypeList(
                CommonEnums.LawDict.EXAMQUESTIONTYPE,CommonEnums.LawRoot.LAWROOT);
        List sysExamQuestionTypeList = new ArrayList();
        for (int j = 0; j < examQuestionType.size() ; j++) {
            Map map = (Map)examQuestionType.get(j);
            if(map.get("sysFlag").toString().equals("自动")){
                sysExamQuestionTypeList.add(examQuestionType.get(j));
            }
        }
        return sysExamQuestionTypeList;
    }
    /**
     * 获取人工判分的题型的信息
     * name(题型名称) code(题型code) examScore(分值) sysFlag(是否自动判分的标识)
     * @return List&lt;Map&lt;String,Object&gt;&gt;
     */
    @Override
    public List getSynExamQuestionTypeList(){
        //题目类型
        List examQuestionType = rEnumUtil.getExamQuestionTypeList(
                CommonEnums.LawDict.EXAMQUESTIONTYPE,CommonEnums.LawRoot.LAWROOT);
        List sysExamQuestionTypeList = new ArrayList();
        for (int j = 0; j < examQuestionType.size() ; j++) {
            Map map = (Map)examQuestionType.get(j);
            if(map.get("sysFlag").toString().equals("人工")){
                sysExamQuestionTypeList.add(examQuestionType.get(j));
            }
        }
        return sysExamQuestionTypeList;
    }

    @Override
    public void commonStatusBatch(String status, Map<String, Object> mapIds){
        Map<String, Object> map = new HashMap<>(2);
        map.put("list", mapIds.get("ids"));
        map.put("status", status);
        baseMapper.commonStatusBatch(map);

    }

    @Override
    public void commonStatusAll(String status){
        if (StringUtils.isNotBlank(status)) {
            baseMapper.commonStatusAll(status);
        }

    }
    @Override
    public void commonUpdateDelFlagBatch(String delFlag, Map<String, Object> mapIds){
        Map<String, Object> map = new HashMap<>(2);
        map.put("list", mapIds.get("ids"));
        map.put("delFlag", delFlag);
        baseMapper.commonUpdateDelFlagBatch(map);

    }

    @Override
    public void commonUpdateDelFlagAll(String delFlag){
        if (StringUtils.isNotBlank(delFlag)) {
            baseMapper.commonUpdateDelFlagAll(delFlag);
        }

    }

}