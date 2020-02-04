package com.css.modules.exam.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.*;
import com.css.modules.exam.dao.LawExamPlanTDao;
import com.css.modules.exam.entity.LawExamPlanTEntity;
import com.css.modules.exam.entity.LawExamUserTEntity;
import com.css.modules.exam.service.LawExamPlanTService;
import com.css.modules.exam.service.LawExamTestTService;
import com.css.modules.exam.service.LawExamUserTService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("lawExamPlanTService")
public class LawExamPlanTServiceImpl extends ServiceImpl<LawExamPlanTDao, LawExamPlanTEntity> implements LawExamPlanTService {
    @Autowired
    private LawExamUserTService lawExamUserTService;
    @Autowired
    private LawExamTestTService lawExamTestTService;
    @Autowired
    private PushMessageUtils pushMessageUtils;
    @Autowired
    FileUtils fileUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String testId = (String)params.get("testName");
        String examTime = (String)params.get("examTime");

        IPage<LawExamPlanTEntity> page = this.page(
                new Query<LawExamPlanTEntity>().getPage(params),
                new QueryWrapper<LawExamPlanTEntity>()
                        .like(StringUtils.isNotBlank(testId),"TEST_ID", testId)
                        .eq(StringUtils.isNotBlank(examTime),"EXAM_TIME", examTime)
                        .ne("DEL_FLAG", "1")

        );
        //根据考试日期 和 具体时间 改变返回前段的考试状态 并 将状态在数据库中存储
        List<LawExamPlanTEntity> lawExamPlanTEntities = page.getRecords();
        this.updateExamPlanStatus(lawExamPlanTEntities);


        return new PageUtils(page);
    }


    /**
     * 根据考试日期 和 具体时间 改变返回前段的考试状态 并 将状态在数据库中存储
     * @param lawExamPlanTEntities
     * @return
     */
    @Override
    public void updateExamPlanStatus( List<LawExamPlanTEntity> lawExamPlanTEntities){
        List<LawExamPlanTEntity> lawExamPlanTEntitiesToSave = new ArrayList<>();
        for (int i = 0; i < lawExamPlanTEntities.size(); i++) {
            String status = lawExamPlanTEntities.get(i).getStatus();
            //如果考试状态已经结束 不进行改变
            if(!"2".equals(status)){
                Date examDay = lawExamPlanTEntities.get(i).getExamTime();
                Date examStartTime = lawExamPlanTEntities.get(i).getExamStartTime();
                Date examEndTime = lawExamPlanTEntities.get(i).getExamEndTime();
                if(null != examDay && null!=examStartTime && null != examEndTime){
                    String examDayStr =  DateUtils.format(examDay,"yyyy-MM-dd");
                    String examStartTimeStr = DateUtils.format(examStartTime,"HH:mm:ss");
                    String examEndTimeStr = DateUtils.format(examEndTime,"HH:mm:ss");
                    String examStartDayTimeStr = examDayStr+ " " + examStartTimeStr;
                    String examEndDayTimeStr = examDayStr + " "+ examEndTimeStr;
                    String currentTimeStr = DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
                    long gapStartTime =   DateUtils.calculateTimeGapSecond(currentTimeStr,examStartDayTimeStr);
                    long gapEndTime = DateUtils.calculateTimeGapSecond(currentTimeStr,examEndDayTimeStr);
                    if(0 <= gapStartTime){
                        //如果当前日期与考试日期相同 当前时间在考试时间早 考试状态为未开始
                        lawExamPlanTEntities.get(i).setStatus("0");
                    }else if( 0 > gapStartTime && 0<gapEndTime){
                        //如果当前日期与考试日期相同 当前时间在考试时间中 考试状态为考试中
                        lawExamPlanTEntities.get(i).setStatus("1");
                    }else if(0 >= gapEndTime){
                        //如果当前日期与考试日期相同 当前时间在考试时间晚 考试状态为已结束
                        lawExamPlanTEntities.get(i).setStatus("2");
                    }
                    lawExamPlanTEntitiesToSave.add(lawExamPlanTEntities.get(i));
                }
            }
        }

        if(0!=lawExamPlanTEntitiesToSave.size()){
            this.updateBatchById(lawExamPlanTEntitiesToSave);
        }
    }






//    /**
//     * 废弃 生成试卷
//     * @param file
//     * @param params
//     * @return
//     */
//    public String createTestPlan(MultipartFile file,Map<String, Object> params) throws Exception {
//        Map<String, Object> map = checkLawExamPlanTData(params);
//        if(StringUtils.isNotBlank((String)map.get("message")) && "ok".equals((String)map.get("message"))){
//            LawExamPlanTEntity lawExamPlanT = (LawExamPlanTEntity) map.get("lawExamPlanTEntity");
//            String testId = lawExamPlanT.getTestId();
//            //考试计划第一次生成 没有id
//            if(StringUtils.isBlank((String)params.get("id"))){
//                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//                lawExamPlanT.setId(uuid);
//                lawExamPlanT.setDelFlag("0");
//                //根据考试计划 中的 试卷ID 修改 试卷状态 为已使用
//                List list = new ArrayList();
//                list.add(testId);
//                Map ids = new HashedMap();
//                ids.put("ids", list);
//                lawExamTestTService.commonStatusBatch("1", ids);
//                //保存考试计划信息
//                this.save(lawExamPlanT);
//                //根据考生EXCEL 保存考生信息
//                String message =lawExamUserTService.importLawExamUserTByExamPlan(file, lawExamPlanT);
//                return message;
//            }else{
//                //重新生成考试计划 考生的信息 和试卷状态 根据考试计划ID删除后重新添加
//                //修改考试计划 有id
//                String uuid = (String)params.get("id");
//                lawExamPlanT.setId(uuid);
//
//                //处理旧数据 --start--
//                //根据考试计划ID 获取上次考试计划的信息
//                LawExamPlanTEntity oldLawExamPlanT =  this.getById(uuid);
//                //根据 上次考试计划 中的 试卷ID 修改 上次使用的 试卷状态 为未使用
//                String oldTestId = oldLawExamPlanT.getTestId();
//                List oldList = new ArrayList();
//                oldList.add(oldTestId);
//                Map oldIds = new HashedMap();
//                oldIds.put("ids", oldList);
//                lawExamTestTService.commonStatusBatch("0", oldIds);
//                //根据 上次考试计划 中的 考试计划ID 删除 上次考试考生的信息
//                List<LawExamUserTEntity> lawExamUserTEntityList = lawExamUserTService.list(new QueryWrapper<LawExamUserTEntity>()
//                        .select("ID")
//                        .eq("PLAN_ID",uuid)
//                        .ne("DEL_FLAG", "1")
//                );
//                List oldExamUserList = new ArrayList();
//                lawExamUserTEntityList.forEach(lawExamUserTEntity ->  oldExamUserList.add(lawExamUserTEntity.getId()));
//                Map oldExamUserIds = new HashedMap();
//                oldExamUserIds.put("ids", oldExamUserList);
//                lawExamUserTService.commonUpdateDelFlagBatch("1",oldExamUserIds);
//                //处理旧数据 --end--
//
//                //处理新数据 --start--
//                //根据新的考试计划 中的 试卷ID 修改 试卷状态 为已使用
//                List list = new ArrayList();
//                list.add(testId);
//                Map ids = new HashedMap();
//                ids.put("ids", list);
//                lawExamTestTService.commonStatusBatch("1", ids);
//                //修改考试计划信息
//                this.updateById(lawExamPlanT);
//                //根据考生EXCEL 保存考生信息
//                String message =lawExamUserTService.importLawExamUserTByExamPlan(file, lawExamPlanT);
//                //处理新数据 --end--
//                return message;
//            }
//        }else {
//            return (String)map.get("message");
//        }
//    }

    /**
     * 生成试卷
     * @param file
     * @param params
     * @return
     */
    @Override
    public String createTestPlan(MultipartFile file,Map<String, Object> params) throws Exception {
        Map<String, Object> map = checkLawExamPlanTData(params);
        if(StringUtils.isNotBlank((String)map.get("message")) && "ok".equals((String)map.get("message"))){
            LawExamPlanTEntity lawExamPlanT = (LawExamPlanTEntity) map.get("lawExamPlanTEntity");
            String testId = lawExamPlanT.getTestId();
            String fileNameUuid = UUID.randomUUID().toString().replaceAll("-", "");
            String filename = file.getOriginalFilename();
            String newFileName = fileNameUuid +"." + StringUtils.substringAfterLast(filename,".");
            //考试计划第一次生成 没有id
            if(StringUtils.isBlank((String)params.get("id"))){
                //存储考生名单excel 生成特定uuid 存储到考试计划表中
                String uploadFileMessage =  fileUtils.uploadFile(file,newFileName);
                if("上传成功!".equals(uploadFileMessage)){
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    lawExamPlanT.setId(uuid);
                    lawExamPlanT.setDelFlag("0");
                    lawExamPlanT.setPushMsgFlag("0");
                    lawExamPlanT.setListId(fileNameUuid);
                    lawExamPlanT.setListName(filename);
                    lawExamPlanT.setListSaveName(newFileName);
                    lawExamPlanT.setListUrl(fileUtils.getFileRootPath()+newFileName);
                    //根据考试计划 中的 试卷ID 修改 试卷状态 为已使用
                    List list = new ArrayList();
                    list.add(testId);
                    Map ids = new HashedMap();
                    ids.put("ids", list);
                    lawExamTestTService.commonStatusBatch("1", ids);
                    //保存考试计划信息
                    this.save(lawExamPlanT);
                    return "生成考试计划成功!";
                }else {
                    return uploadFileMessage;
                }
            }else{
                //重新生成考试计划 考生的信息 和试卷状态 根据考试计划ID删除后重新添加
                //修改考试计划 有id
                String uuid = (String)params.get("id");

                //处理旧数据 --start--
                //根据考试计划ID 获取上次考试计划的信息
                LawExamPlanTEntity oldLawExamPlanT =  this.getById(uuid);
                //根据 上次考试计划 中的 试卷ID 修改 上次使用的 试卷状态 为未使用
                String oldTestId = oldLawExamPlanT.getTestId();
                List oldList = new ArrayList();
                oldList.add(oldTestId);
                Map oldIds = new HashedMap();
                oldIds.put("ids", oldList);
                lawExamTestTService.commonStatusBatch("0", oldIds);
                //处理旧数据 --end--

                //处理新数据 --start--
                //根据新的考试计划 中的 试卷ID 修改 试卷状态 为已使用
                List list = new ArrayList();
                list.add(testId);
                Map ids = new HashedMap();
                ids.put("ids", list);
                lawExamTestTService.commonStatusBatch("1", ids);
                //修改考试计划信息
                lawExamPlanT.setId(uuid);
                //文件名是原来的名字则覆盖原文件 否则 删除原来的文件 上传新的文件
                String uploadFileMessage;
                if(oldLawExamPlanT.getListName().equals(filename)){
                    uploadFileMessage =  fileUtils.uploadFile(file,oldLawExamPlanT.getListSaveName());
                }else{
                    fileUtils.deleteFile(oldLawExamPlanT.getListSaveName());
                    uploadFileMessage =  fileUtils.uploadFile(file,newFileName);
                    lawExamPlanT.setListId(fileNameUuid);
                    lawExamPlanT.setListName(filename);
                    lawExamPlanT.setListSaveName(newFileName);
                    lawExamPlanT.setListUrl(fileUtils.getFileRootPath()+newFileName);
                }
                this.updateById(lawExamPlanT);

                if("上传成功!".equals(uploadFileMessage)){
                    return "修改考试计划成功!";
                }else {
                    return "上传文件成功!";
                }
                //处理新数据 --end--

            }

        }else {
            return (String)map.get("message");
        }
    }

    /**
     *  不传文件 修改试卷
     * @param params
     * @return
     */
    @Override
    public String updateTesPlan(Map<String, Object> params) throws Exception{
        Map<String, Object> map = checkLawExamPlanTData(params);
        if(StringUtils.isNotBlank((String)map.get("message")) && "ok".equals((String)map.get("message"))){
            LawExamPlanTEntity lawExamPlanT = (LawExamPlanTEntity) map.get("lawExamPlanTEntity");
            String testId = lawExamPlanT.getTestId();
            //考试计划id不能为空
            if(StringUtils.isNotBlank((String)params.get("id"))){
                //重新生成考试计划 考生的信息 和试卷状态 根据考试计划ID删除后重新添加
                //修改考试计划 有id
                String uuid = (String)params.get("id");
                lawExamPlanT.setId(uuid);

                //处理旧数据 --start--
                //根据考试计划ID 获取上次考试计划的信息
                LawExamPlanTEntity oldLawExamPlanT =  this.getById(uuid);
                //根据 上次考试计划 中的 试卷ID 修改 上次使用的 试卷状态 为未使用
                String oldTestId = oldLawExamPlanT.getTestId();
                List oldList = new ArrayList();
                oldList.add(oldTestId);
                Map oldIds = new HashedMap();
                oldIds.put("ids", oldList);
                lawExamTestTService.commonStatusBatch("0", oldIds);
                //处理旧数据 --end--

                //处理新数据 --start--
                //根据新的考试计划 中的 试卷ID 修改 试卷状态 为已使用
                List list = new ArrayList();
                list.add(testId);
                Map ids = new HashedMap();
                ids.put("ids", list);
                lawExamTestTService.commonStatusBatch("1", ids);
                //修改考试计划信息
                this.updateById(lawExamPlanT);
                //处理新数据 --end--
                return "修改考试计划成功!";
            }else{
                return "考试计划id不能为空!";
            }

        }else {
            return (String)map.get("message");
        }
    }



    private Map<String, Object> checkLawExamPlanTData(Map<String, Object> params) throws Exception{
        String testId = (String)params.get("testId");
        String testName = (String)params.get("testName");
        String examTime = (String)params.get("examTime");
        String examStartTime = (String)params.get("examStartTime");
        String examEndTime = (String)params.get("examEndTime");
        String limitTime = (String)params.get("limitTime");
        String listId = (String)params.get("listId");
        String listUrl = (String)params.get("listUrl");
        String joinNum = (String)params.get("joinNum");
        String status = (String)params.get("status");
        if(StringUtils.isNotBlank(testId)){
            if(StringUtils.isNotBlank(testName)) {
                if (StringUtils.isNotBlank(examTime)) {
                    if (StringUtils.isNotBlank(examStartTime)) {
                        if (StringUtils.isNotBlank(examEndTime)) {
                            if (StringUtils.isNotBlank(limitTime)) {
                                if (StringUtils.isNotBlank(joinNum)) {
                                    LawExamPlanTEntity lawExamPlanTEntity = new LawExamPlanTEntity();
                                    lawExamPlanTEntity.setTestId(testId);
                                    lawExamPlanTEntity.setTestName(testName);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    lawExamPlanTEntity.setExamTime(dateFormat.parse(examTime));
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                                    lawExamPlanTEntity.setExamStartTime(timeFormat.parse(examStartTime));
                                    lawExamPlanTEntity.setExamEndTime(timeFormat.parse(examEndTime));
                                    lawExamPlanTEntity.setLimitTime(Integer.valueOf(limitTime));
                                    lawExamPlanTEntity.setJoinNum(Integer.valueOf(joinNum));

                                    lawExamPlanTEntity.setListId(listId);
                                    lawExamPlanTEntity.setListUrl(listUrl);
                                    lawExamPlanTEntity.setStatus(status);
                                    params.put("message","ok");
                                    params.put("lawExamPlanTEntity",lawExamPlanTEntity);
                                    return  params;
                                } else {
                                    params.put("message","考生人数不能为空");
                                    return params;
                                }
                            } else {
                                params.put("message","答题时长不能为空");
                                return params;
                            }
                        } else {
                            params.put("message","考试结束时间不能为空");
                            return params;
                        }
                    } else {
                        params.put("message","考试开始时间不能为空");
                        return params;
                    }
                } else {
                    params.put("message","考试日期不能为空");
                    return params;
                }
            }else {
                params.put("message","考试名称不能为空");
                return params;
            }
        }else {
            params.put("message","考试ID不能为空");
            return params;
        }
    }

    /**
     * 通知考生
     * @param mapIds 考试计划ID
     */
    @Override
    public String pushMessage(Map<String, Object> mapIds){

        StringBuffer resultMessage = new StringBuffer();
        //获取ID值
        List ids = (List) mapIds.get("ids");
        if(0 != ids.size()){
            for (int i = 0; i < ids.size(); i++) {
                //修改考试计划中的发短信状态
                LawExamPlanTEntity lawExamPlanTEntity = this.getById(ids.get(i).toString());
                lawExamPlanTEntity.setPushMsgFlag("1");
                // 解析考试计划对应的 excel 添加到考生信息表 获取电话 发短信
                try {
                    //根据考生EXCEL 保存考生信息
                    File file = new File(fileUtils.getFileRootPath()+lawExamPlanTEntity.getListSaveName());
                    FileInputStream fileInputStream = new FileInputStream(file);
                    MultipartFile multipartFile = new MockMultipartFile(file.getName(),fileInputStream);
                    //MultipartFile multipartFile =null;
                   String importMessage =lawExamUserTService.importLawExamUserTByExamPlan(multipartFile, lawExamPlanTEntity);

                    if("导入成功".equals(importMessage)){
                        //获取电话 发短信
                        List phonelist = new ArrayList();
                        List<LawExamUserTEntity> excelLawExamUserTlist = ExportExcelUtil.importExcel(multipartFile, 0, 1, LawExamUserTEntity.class);
                        for (int j = 0; j < excelLawExamUserTlist.size();j++) {
                            String contactInfo = excelLawExamUserTlist.get(j).getContactInfo();
                            if(StringUtils.isNotBlank(contactInfo)){
                                phonelist.add(contactInfo);
                            }
                        }
                        String title = "执法监督考试";

                        StringBuffer messagebody = new StringBuffer();
                        messagebody.append("您参加了");
                        messagebody.append(lawExamPlanTEntity.getTestName());
                        messagebody.append("考试,");
                        messagebody.append("考试时间为");
                        messagebody.append( DateUtils.format(lawExamPlanTEntity.getExamTime(),"yyyy"));
                        messagebody.append(" ");
                        messagebody.append(DateUtils.format(lawExamPlanTEntity.getExamStartTime(),"HH:mm:ss"));
                        messagebody.append("~");
                        messagebody.append(DateUtils.format(lawExamPlanTEntity.getExamEndTime(),"HH:mm:ss"));
                        messagebody.append(",");
                        messagebody.append("请及时在线上参加考试!");

                        Object object = pushMessageUtils.pushMessage(phonelist,messagebody.toString(),title);
                        Map result = (Map)object;

                        if(result.keySet().contains("faildResult")){
                            List list = (List)result.get("faildResult");
                            if(0 == list.size()){
                                //传文件到文件服务器
                                String uploadFileResult = fileUtils.httpClientUploadFile(file.getName());
                                JSONObject jsonObject = JSONObject.parseObject(uploadFileResult);
                                if (null != jsonObject && "上传成功".equals(jsonObject.get("message"))) {
                                    //保存考试计划表中的文件url
                                    lawExamPlanTEntity.setListUrl(jsonObject.get("url").toString());
                                    this.updateById(lawExamPlanTEntity);
                                    //将本地文件删除
                                    fileUtils.deleteFile(file.getName());
                                    resultMessage.append("第"+(i+1)+"个考生名单发送成功!");
                                }else{
                                    resultMessage.append("第"+(i+1)+"个考生名单文件上传至文件服务器失败！");
                                }
                            }else{
                                resultMessage.append("第"+(i+1)+"个考生名单的手机号格式不正确：" + result.get("faildResult").toString());
                            }
                        }else{
                            if(result.keySet().contains("msg")){
                                resultMessage.append("第"+(i+1)+"个考生名单的短信通知：" + result.get("msg").toString());
                            }else{
                                resultMessage.append("获取第"+(i+1)+"个考生名单的短信通知发送失败！");
                            }
                        }
                    }else{
                        resultMessage.append("获取第"+(i+1)+"个考试名单文件失败！");
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                    return e.getMessage();
                }
            }

        }else{
            return "id不能为空！";
        }

        return resultMessage.toString();

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