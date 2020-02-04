package com.css.modules.jgzfkuserinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.enums.CommonEnums;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.css.modules.jgzfkuserinfo.dao.JgZfkUserInfoDao;
import com.css.modules.jgzfkuserinfo.entity.JgZfkUserInfoEntity;
import com.css.modules.jgzfkuserinfo.service.JgZfkUserInfoService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 执法检查人员库
 *
 * @author
 * @email
 * @date 2019-11-14 14:02:12
 */
@Service("JgZfkUserInfoService")
public class JgZfkUserInfoServiceImpl extends ServiceImpl<JgZfkUserInfoDao, JgZfkUserInfoEntity> implements JgZfkUserInfoService {

    @Autowired
    private REnumUtil rEnumUtil;
    @Autowired
    private AreaUtils areaUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String agentname = (String)params.get("agentname");
        String code = (String)params.get("code");
        String pubStatus = (String)params.get("pubStatus");
        IPage<JgZfkUserInfoEntity> page = this.page(
                new Query<JgZfkUserInfoEntity>().getPage(params),
                new QueryWrapper<JgZfkUserInfoEntity>()
                        .like(StringUtils.isNotBlank(agentname),"AGENTNAME", agentname)
                        .like(StringUtils.isNotBlank(code),"CODE", code)
                        .eq(StringUtils.isNotBlank(pubStatus),"ISSUE_STATUS", pubStatus)
                        .ne("DEL_FLAG", "1")
        );

        List<JgZfkUserInfoEntity> list = page.getRecords();
        if (null != list && 0 != list.size()) {
            list.forEach(userInfo->userInfo.setId(userInfo.getRecordUniqueIdentity()));
            page.setRecords(list);
        }
        return new PageUtils(page);
    }
    @Override
    public void commonUpdatePubStatusBatch(String pubStatus, Map<String, Object> mapIds){
        Map<String, Object> map = new HashMap(2);
        map.put("list", mapIds.get("ids"));
        map.put("pubStatus", pubStatus);
        baseMapper.commonUpdatePubStatusBatch(map);

    }

    @Override
    public void commonUpdatePubStatusAll(String pubStatus){
        if (StringUtils.isNotBlank(pubStatus)) {
            baseMapper.commonUpdatePubStatusAll(pubStatus);
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
    @Override
    public void saveJgZfkUserInfo(JgZfkUserInfoEntity jgZfkUserInfo){
        if (StringUtils.isNotBlank(jgZfkUserInfo.getRecordUniqueIdentity())) {
            jgZfkUserInfo.setRecordUniqueIdentity(null);
            jgZfkUserInfo.setPubStatus("0");
            jgZfkUserInfo.setPubDate(null);
        }
        this.save(jgZfkUserInfo);
    }
    @Override
    public void updateJgZfkUserInfo(JgZfkUserInfoEntity jgZfkUserInfo){

        this.updateById(jgZfkUserInfo);
    }
    @Override
    public String importJgZfkUserInfo(MultipartFile file) throws Exception{
        List<JgZfkUserInfoEntity> excelJgZfkUserInfolist = ExportExcelUtil.importExcel(file, 0, 1, JgZfkUserInfoEntity.class);
        List politicalStatusCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.POLITICALSTATUS,CommonEnums.LawRoot.LAWROOT);
        List nationCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.OtherDict.NATIONALITY,CommonEnums.OtherRoot.DROOT);
        List highestEducationCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.OtherDict.EDULEAVE,CommonEnums.OtherRoot.DROOT);
        List lawStaffTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWSTAFFTYPE,CommonEnums.LawRoot.LAWROOT);
        List provinceCodeAndNamelist = areaUtils.getProvince();
        for (int i = 0; i < excelJgZfkUserInfolist.size(); i++) {
            excelJgZfkUserInfolist.get(i).setRecordUniqueIdentity(null);
            excelJgZfkUserInfolist.get(i).setPubStatus("0");
            excelJgZfkUserInfolist.get(i).setPubDate(null);
            //字典项转换 name 转为 code 值
            //政治面貌
            String codeOfPoliticalStatus = rEnumUtil.convertDict("name",excelJgZfkUserInfolist.get(i).getPoliticalStatus(),"code",
                    politicalStatusCodeAndNamelist);
            excelJgZfkUserInfolist.get(i).setPoliticalStatus(codeOfPoliticalStatus);
            //民族
            String codeOfNation = rEnumUtil.convertDict("name",excelJgZfkUserInfolist.get(i).getNation(),"code",
                    nationCodeAndNamelist);
            excelJgZfkUserInfolist.get(i).setNation(codeOfNation);
            //最高学历
            String codeOfHighestEducation = rEnumUtil.convertDict("name",excelJgZfkUserInfolist.get(i).getHighestEducation(),"code",
                    highestEducationCodeAndNamelist);
            excelJgZfkUserInfolist.get(i).setHighestEducation(codeOfHighestEducation);
            //执法人员性质
            String codeOfLawStaffType = rEnumUtil.convertDict("name",excelJgZfkUserInfolist.get(i).getLawStaffType(),"code",
                    lawStaffTypeCodeAndNamelist);
            excelJgZfkUserInfolist.get(i).setLawStaffType(codeOfLawStaffType);
            //来源
            String codeOfarea = areaUtils.convertDict("administrativeAreaName",excelJgZfkUserInfolist.get(i).getProvince(),"bmAreaUuid",
                    provinceCodeAndNamelist);
            excelJgZfkUserInfolist.get(i).setProvince(codeOfarea);
            ValidatorUtils.validateEntity(excelJgZfkUserInfolist.get(i), AddGroup.class);
        }
        if (excelJgZfkUserInfolist.size() != 0) {
            this.saveBatch(excelJgZfkUserInfolist);
        }
        return "导入成功";

    }
    /**
     * 下载全部
     * @return
     */
    @Override
    public List<JgZfkUserInfoEntity> downloadAll(){
        List<JgZfkUserInfoEntity> list = this.list(new QueryWrapper<JgZfkUserInfoEntity>()
                .ne("DEL_FLAG", "1"));
        list = this.praceCodeToName(list);
        return list;
    }

    /**
     * 批量下载
     * @return
     */
    @Override
    public List<JgZfkUserInfoEntity> downloadBatch(Map<String, Object> mapIds){
        //      直接获取map中存的数组map={"ids":[1,2,3]}
        List<Object> vlist = (List<Object>) mapIds.get("ids");
//      list 转化为数组
        Object[] ids = vlist.toArray(new Object[vlist.size()]);
//      Object... values代表Object[]
        List<JgZfkUserInfoEntity> list = this.list(new QueryWrapper<JgZfkUserInfoEntity>()
                .in(!mapIds.isEmpty(), "ROWGUID", ids)
                .ne("DEL_FLAG", "1"));
        list = this.praceCodeToName(list);
        return list;
    }

    /**
     * 下载EXCEL时 字典项转换
     *
     * @param list
     * @return
     */
    private List<JgZfkUserInfoEntity> praceCodeToName(List<JgZfkUserInfoEntity> list){
        List politicalStatusCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.POLITICALSTATUS,CommonEnums.LawRoot.LAWROOT);
        List nationCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.OtherDict.NATIONALITY,CommonEnums.OtherRoot.DROOT);
        List highestEducationCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.OtherDict.EDULEAVE,CommonEnums.OtherRoot.DROOT);
        List lawStaffTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWSTAFFTYPE,CommonEnums.LawRoot.LAWROOT);
        List provinceCodeAndNamelist = areaUtils.getProvince();

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRecordUniqueIdentity(Integer.toString(i + 1));
            //字典项转换 name 转为 name 值
            //政治面貌
            String nameOfPoliticalStatus = rEnumUtil.convertDict("code",list.get(i).getPoliticalStatus(),"name",
                    politicalStatusCodeAndNamelist);
            list.get(i).setPoliticalStatus(nameOfPoliticalStatus);
            //民族
            String nameOfNation = rEnumUtil.convertDict("code",list.get(i).getNation(),"name",
                    nationCodeAndNamelist);
            list.get(i).setNation(nameOfNation);
            //最高学历
            String nameOfHighestEducation = rEnumUtil.convertDict("code",list.get(i).getHighestEducation(),"name",
                    highestEducationCodeAndNamelist);
            list.get(i).setHighestEducation(nameOfHighestEducation);
            //执法人员性质
            String nameOfLawStaffType = rEnumUtil.convertDict("code",list.get(i).getLawStaffType(),"name",
                    lawStaffTypeCodeAndNamelist);
            list.get(i).setLawStaffType(nameOfLawStaffType);
            //来源
            String nameOfarea = areaUtils.convertDict("bmAreaUuid",list.get(i).getProvince(),"administrativeAreaName",
                    provinceCodeAndNamelist);
            list.get(i).setProvince(nameOfarea);
        }
        return list;

    }

    /**
     * 按执法年龄段分布
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getAgeGroupStatistics(List<JgZfkUserInfoEntity> list){
        //数据的总数
        long sumAgeGroup = list.stream()
                .filter(jgZfkUserInfo -> {
                    String ageGroup = jgZfkUserInfo.getAgeGroup();
                    return StringUtils.isBlank(ageGroup)?false:true;
                }).count();
        List ageStandadList = new ArrayList();
        ageStandadList.add("30岁以下");
        ageStandadList.add("31~40岁");
        ageStandadList.add("41~50岁");
        ageStandadList.add("51~60岁");
        ageStandadList.add("61~65岁");
        ageStandadList.add("66岁以上");
        Map<Object, Long> collectAge = list.stream()
                .filter(jgZfkUserInfo -> {
                    String ageGroup = jgZfkUserInfo.getAgeGroup();
                    return StringUtils.isBlank(ageGroup)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                JgZfkUserInfoEntity::getAgeGroup,Collectors.counting()
                        )
                );
        List<JSONObject> jsonObjectAgeGroupList = StatisticsUtils.getResultWithProportion(sumAgeGroup,collectAge,ageStandadList);

        return jsonObjectAgeGroupList;
    }

     /**
     * 按执法证件的有效性统计
     * @param list
     * @returnl
     */
     @Override
     public List<JSONObject> getIsEffectiveStatistics(List<JgZfkUserInfoEntity> list){
         List standadList = new ArrayList();
         standadList.add("在有效期内");
         standadList.add("不在有效期内");
         long sumIsEffective = list.stream()
                 .filter(jgZfkUserInfo -> {
                     String isEffective = jgZfkUserInfo.getIsEffective();
                     return StringUtils.isBlank(isEffective)?false:true;
                 }).count();
         Map<Object, Long> collectEffective = list.stream()
                 .filter(jgZfkUserInfo -> {
                     String isEffective = jgZfkUserInfo.getIsEffective();
                     return StringUtils.isBlank(isEffective)?false:true;
                 })
                 .collect(Collectors.
                         groupingBy(
                                 JgZfkUserInfoEntity::getIsEffective,Collectors.counting()
                         )
                 );
         List<JSONObject> jsonObjectEffectiveList = StatisticsUtils.getResultWithProportion(sumIsEffective,collectEffective,standadList);
         return jsonObjectEffectiveList;
     }
    /**
     * 获取按省份名称分组的数据
     * @param list 执法事项list
     * @returnl
     */
    @Override
    public List<JSONObject> getProvinceStatistics(List<JgZfkUserInfoEntity> list){
        //数据的总数
        long sumArea = list.stream()
                .filter(jgZfkUserInfo -> {
                    String province = jgZfkUserInfo.getProvince();
                    return StringUtils.isBlank(province)?false:true;
                }).count();
        Map<Object, Long> collectArea = list.stream()
                .filter(jgZfkUserInfo -> {
                    String province = jgZfkUserInfo.getProvince();
                    return StringUtils.isBlank(province)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                JgZfkUserInfoEntity::getProvince,Collectors.counting())
                );
        List<JSONObject> jsonObjectAreaList = StatisticsUtils.getResultWithProportion(sumArea,collectArea);

        return jsonObjectAreaList;

    }
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    @Override
     public List<JgZfkUserInfoEntity> getProcessedData(List<JgZfkUserInfoEntity> list){
        List provinceCodeAndNamelist = areaUtils.getProvince();
        list.forEach(jgZfkUserInfo ->{
            //字典项转换
            //来源
            String provinceCode = jgZfkUserInfo.getProvince();
            String codeOfProvince = areaUtils.convertDict("bmAreaUuid",provinceCode,"administrativeAreaName",
                    provinceCodeAndNamelist);
            jgZfkUserInfo.setProvince(codeOfProvince);
            //年龄段处理
            if (null != jgZfkUserInfo.getBirthDate() ) {
                String birthday = DateUtils.format(jgZfkUserInfo.getBirthDate(),"yyyy-MM-dd");
                int age = DateUtils.getAgeByBirthDay(birthday);
                String ageGroup = "";
                if(30 >= age){
                    ageGroup = "30岁以下";
                }else if(31 <= age && 40 >= age ){
                    ageGroup = "31~40岁";
                }else if(41 <= age && 50 >= age ){
                    ageGroup = "41~50岁";
                }else if(51 <= age && 60 >= age ){
                    ageGroup = "51~60岁";
                }else if(61 <= age && 65 >= age ){
                    ageGroup = "61~65岁";
                }else if(66 <= age ){
                    ageGroup = "66岁以上";
                }
                jgZfkUserInfo.setAgeGroup(ageGroup);
            }
            //有效期处理
            Date tody = new Date();
            Date validityDocuments = jgZfkUserInfo.getValidityDocuments();
            if (null != validityDocuments ) {
                float d = DateUtils.calculateTimeGapDay(tody,validityDocuments);
                if (d >= 0) {
                    jgZfkUserInfo.setIsEffective("在有效期内");
                }else {
                    jgZfkUserInfo.setIsEffective("不在有效期内");
                }
            }


        });
        return list;
     }
    /**
     * 根据省份 获取直返人员全部列表
     * @param province 省份
     * @returnl
     */
    @Override
    public List<JgZfkUserInfoEntity> queryByProvince(String province){
        //来源
        List provinceCodeAndNamelist = areaUtils.getProvince();
        String codeOfProvince = areaUtils.convertDict("administrativeAreaName",province,"bmAreaUuid",
                provinceCodeAndNamelist);
        List<JgZfkUserInfoEntity> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(codeOfProvince)){
            list = this.list(new QueryWrapper<JgZfkUserInfoEntity>()
                .select("AGENTNAME","UNIT","CODE","VALIDITY_DOCUMENTS")
                .eq("PROVINCE", codeOfProvince)
                .ne("DEL_FLAG", "1"));
        }
        return list;
    }


    /**
     * 根据省份证号 获取直返人员全部列表
     * @param idNumber 身份证号
     * @returnl
     */
    @Override
    public List<JgZfkUserInfoEntity> queryByIdNumber(String idNumber){
        List<JgZfkUserInfoEntity> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(idNumber)){
            list = this.list(new QueryWrapper<JgZfkUserInfoEntity>()
                    .eq("ID_NUMBER", idNumber)
                    .ne("DEL_FLAG", "1"));
        }
        return list;
    }
}