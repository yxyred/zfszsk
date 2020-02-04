package com.css.modules.jgzskcase.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.enums.CommonEnums;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.modules.jgzskcase.dao.JgZskCaseDao;
import com.css.modules.jgzskcase.entity.JgZskCaseEntity;
import com.css.modules.jgzskcase.service.JgZskCaseService;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service("jgZskCaseService")
public class JgZskCaseServiceImpl extends ServiceImpl<JgZskCaseDao, JgZskCaseEntity> implements JgZskCaseService {
    @Autowired
    private REnumUtil rEnumUtil;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String caseName = (String)params.get("caseName");
        String responsibleUnit = (String)params.get("responsibleUnit");
        IPage<JgZskCaseEntity> page = this.page(
                new Query<JgZskCaseEntity>().getPage(params),
                new QueryWrapper<JgZskCaseEntity>()
                .like(StringUtils.isNotBlank(caseName),"CASE_NAME",caseName)
                 .like(StringUtils.isNotBlank(responsibleUnit),"RESPONSIBLE_UNIT",responsibleUnit)
                  .ne("DEL_FLAG", "1")
        );

        return new PageUtils(page);
    }
    @Override
    public String importJgZskCase(MultipartFile file) throws Exception {
        List<JgZskCaseEntity> excelJgZskCaselist = ExportExcelUtil.importExcel(file, 0, 1, JgZskCaseEntity.class);
        //用于字典项转换
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        for (int i = 0; i < excelJgZskCaselist.size(); i++) {
            excelJgZskCaselist.get(i).setDelFlag("0");
            excelJgZskCaselist.get(i).setTypicalCase("0");
            excelJgZskCaselist.get(i).setId(null);
            //字典项转换
            //执法类别 name 转换为 ID
            String lawTypeId = rEnumUtil.convertDict("name",excelJgZskCaselist.get(i).getLawTypeName(),"code",
                    lawTypeCodeAndNamelist);
            excelJgZskCaselist.get(i).setLawTypeId(lawTypeId);
            ValidatorUtils.validateEntity(excelJgZskCaselist.get(i), AddGroup.class);
        }
        if (excelJgZskCaselist.size() != 0) {
            this.saveBatch(excelJgZskCaselist);
        }
        return "导入成功";
    }
    @Override
    public void saveJgZskCase(JgZskCaseEntity jgZskCaseEntity){
        jgZskCaseEntity.setDelFlag("0");
        jgZskCaseEntity.setTypicalCase("0");
        jgZskCaseEntity.setId(null);
        this.save(jgZskCaseEntity);
    }
    @Override
    public void updateJgZskCase(JgZskCaseEntity jgZskCaseEntity){
        this.updateById(jgZskCaseEntity);
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
    public void UpdateTypicalCaseBatch(String typicalCase, Map<String, Object> mapIds){
        Map<String, Object> map = new HashMap<>(2);
        map.put("list", mapIds.get("ids"));
        map.put("typicalCase", typicalCase);
        baseMapper.commonUpdateTypicalCaseBatch(map);

    }
    /**
     * 按执法类别分组的数据,带百分比
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getLawTypeNameStatistics(List<JgZskCaseEntity> list){
        //按执法类别分组的数据,带百分比
        List standadList = rEnumUtil.getNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        long sumLawTypeName = list.stream()
                .filter(jgZskCase -> {
                    String lawTypeName = jgZskCase.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                }).count();
        Map<Object, Long> collectLawTypeNameWithProportion = list.stream()
                .filter(jgZskCase -> {
                    String lawTypeName = jgZskCase.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                })
                .collect(Collectors.
                        groupingBy(
                                JgZskCaseEntity::getLawTypeName,Collectors.counting())
                );
        List<JSONObject> jsonObjectLawTypeNameWithProportionList = StatisticsUtils.getResultWithProportion(sumLawTypeName,collectLawTypeNameWithProportion,standadList);

        return jsonObjectLawTypeNameWithProportionList;
    }
    /**
     * 按执法结果日期分组的数据，不带百分比
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getSettlementDateStrStatistics(List<JgZskCaseEntity> list,int yearStart,int yearEnd){
        //数据的总数
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);
        long sumSettlementDateStr = list.stream()
                .filter(lawProjectListT -> {
                    String settlementDateStr = lawProjectListT.getSettlementDateStr();
                    return StringUtils.isBlank(settlementDateStr)?false:true;
                }).count();
        Map<Object, Long> collectSettlementDate =  list.stream()
                .filter(lawProjectListT -> {
                    String settlementDateStr = lawProjectListT.getSettlementDateStr();
                    return StringUtils.isBlank(settlementDateStr)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                JgZskCaseEntity::getSettlementDateStr,Collectors.counting())
                );
        List<JSONObject> jsonObjectSettlementDateList = StatisticsUtils.getResultWithoutProportion(sumSettlementDateStr,collectSettlementDate,yearsList);
        return jsonObjectSettlementDateList;
    }

    /**
     * 按执法类别 统计近五年的年度数据
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getLawTypeNameWithYearsStatistics(List<JgZskCaseEntity> list,int yearStart,int yearEnd){
        //按执法类别分组的数据,带百分比
        List standadList = rEnumUtil.getNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);
        long sumLawTypeName = list.stream()
                .filter(jgZskCase -> {
                    String lawTypeName = jgZskCase.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                }).count();
        Map<Object, Map<Object,Long>> collectLawTypeNameWithYears = list.stream()
                .filter(jgZskCase -> {
                    String lawTypeName = jgZskCase.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                })
                .collect(Collectors.
                        groupingBy(
                                JgZskCaseEntity::getLawTypeName, Collectors.
                                        groupingBy(JgZskCaseEntity::getSettlementDateStr,Collectors.counting())
                        )
                );
        List<JSONObject> jsonObjectLawTypeNameWithYearsList = StatisticsUtils.getResultWithYears(sumLawTypeName,collectLawTypeNameWithYears,yearsList,standadList);
        return jsonObjectLawTypeNameWithYearsList;
    }

    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    @Override
    public List<JgZskCaseEntity> getProcessedData(List<JgZskCaseEntity> list){
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        list.forEach(lawProjectResult->{
            //将结案日期 格式化到年 放入 settlementDateStr
            String settlementDateStr = DateUtils.format(lawProjectResult.getSettlementDate(),"yyyy");
            lawProjectResult.setSettlementDateStr(settlementDateStr);
            //执法类别
            String lawTypeId = lawProjectResult.getLawTypeId();
            String lawTypeName = rEnumUtil.convertDict("code",lawTypeId,"name",lawTypeCodeAndNamelist);
            lawProjectResult.setLawTypeName(lawTypeName);

        });
        return list;
    }
    /**
     * 下载全部
     * @return
     */
    @Override
    public List<JgZskCaseEntity> downloadAll(){
        List<JgZskCaseEntity> list = this.list(new QueryWrapper<JgZskCaseEntity>()
                .ne("DEL_FLAG", "1"));
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
            //字典项转换 ID 转换为 name
            //执法类别
            String lawTypeName = rEnumUtil.convertDict("code",list.get(i).getLawTypeId(),"name",lawTypeCodeAndNamelist);
            list.get(i).setLawTypeName(lawTypeName);
        }
        return list;
    }

}