package com.css.modules.lawprojectresultt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.enums.CommonEnums;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.modules.jgzskcase.entity.JgZskCaseEntity;
import com.css.modules.lawprojectresultt.dao.LawProjectResultTDao;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import com.css.modules.lawprojectresultt.service.LawProjectResultTService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 执法结果信息
 *
 * @author
 * @email
 * @date 2019-11-14 14:02:12
 */
@Service("lawProjectResultTService")
public class LawProjectResultTServiceImpl extends ServiceImpl<LawProjectResultTDao, LawProjectResultTEntity> implements LawProjectResultTService {
    @Autowired
    private REnumUtil rEnumUtil;
    @Autowired
    private AreaUtils areaUtils;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String lawObject = (String)params.get("lawObject");
        String lawMain = (String)params.get("lawMain");
        String lawTypeId = (String)params.get("lawTypeId");
        String pubStatus = (String)params.get("pubStatus");
        String lawResultTimeStart = (String)params.get("lawResultTimeStart");
        String lawResultTimeEnd = (String)params.get("lawResultTimeEnd");
        boolean flag ;
        if (StringUtils.isNotBlank(lawResultTimeStart) && StringUtils.isNotBlank(lawResultTimeEnd) ){
            flag = true;
        }else{
            flag = false;
        }

        IPage<LawProjectResultTEntity> page = this.page(
                new Query<LawProjectResultTEntity>().getPage(params),
                new QueryWrapper<LawProjectResultTEntity>()
                        .like(StringUtils.isNotBlank(lawObject),"LAW_OBJECT", lawObject)
                        .eq(StringUtils.isNotBlank(lawMain),"LAW_MAIN", lawMain)
                        .eq(StringUtils.isNotBlank(lawTypeId),"LAW_TYPE_ID", lawTypeId)
                        .eq(StringUtils.isNotBlank(pubStatus),"ISSUE_STATUS", pubStatus)
                        .ne("DEL_FLAG", "1")
                        .between(flag,"RESULT_TIME",lawResultTimeStart,lawResultTimeEnd)

        );

        return new PageUtils(page);
    }
    @Override
    public void commonUpdatePubStatusBatch(String pubStatus, Map<String, Object> mapIds){
        Map<String, Object> map = new HashMap<>(2);
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
    public void saveLawProjectResultT(LawProjectResultTEntity lawProjectResultT){
        lawProjectResultT.setId(null);
        lawProjectResultT.setPubStatus("0");
        lawProjectResultT.setPubDate(null);
        this.save(lawProjectResultT);
    }
    @Override
    public void updateLawProjectResultT(LawProjectResultTEntity lawProjectResultT){
        this.updateById(lawProjectResultT);
    }
    @Override
    public String importLawProjectResultT(MultipartFile file) throws Exception{
        List<LawProjectResultTEntity> excelLawProjectResultTlist = ExportExcelUtil.importExcel(file, 0, 1, LawProjectResultTEntity.class);

        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        List deptNameCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.DEPTNAME,CommonEnums.LawRoot.LAWROOT);
        List provinceCodeAndNamelist = areaUtils.getProvince();
        List lawMainCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWMAIN,CommonEnums.LawRoot.LAWROOT);
        for (int i = 0; i < excelLawProjectResultTlist.size(); i++) {
            excelLawProjectResultTlist.get(i).setId(null);
            excelLawProjectResultTlist.get(i).setPubStatus("0");
            excelLawProjectResultTlist.get(i).setPubDate(null);
            //字典项转换 name 转为 name 值
            //执法类别
            String lawTypeId = rEnumUtil.convertDict("name",excelLawProjectResultTlist.get(i).getLawTypeName(),"code",lawTypeCodeAndNamelist);
            excelLawProjectResultTlist.get(i).setLawTypeId(lawTypeId);
            //发布单位
            String deptId = rEnumUtil.convertDict("name",excelLawProjectResultTlist.get(i).getDeptName(),"code",deptNameCodeAndNamelist);
            excelLawProjectResultTlist.get(i).setDeptId(deptId);
            //来源
            String codeOfarea = areaUtils.convertDict("administrativeAreaName",excelLawProjectResultTlist.get(i).getProvince(),"bmAreaUuid",
                    provinceCodeAndNamelist);
            excelLawProjectResultTlist.get(i).setProvince(codeOfarea);
            //执法主体
            String lawMainId = rEnumUtil.convertDict("name",excelLawProjectResultTlist.get(i).getLawMainStr(),"code",
                    lawMainCodeAndNamelist);
            excelLawProjectResultTlist.get(i).setLawMain(lawMainId);
            ValidatorUtils.validateEntity(excelLawProjectResultTlist.get(i), AddGroup.class);
        }
        if (excelLawProjectResultTlist.size() != 0) {
            this.saveBatch(excelLawProjectResultTlist);
        }
        return "导入成功";

    }

    /**
     * 按执法类别分组的数据,带百分比
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getLawTypeNameStatistics(List<LawProjectResultTEntity> list){
        //按执法类别分组的数据,带百分比
        List standadList = rEnumUtil.getNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);

        long sumLawTypeName = list.stream()
                .filter(lawProjectResultT -> {
                    String lawTypeName = lawProjectResultT.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                }).count();
        Map<Object, Long> collectLawTypeNameWithProportion = list.stream()
                .filter(lawProjectResultT -> {
                    String lawTypeName = lawProjectResultT.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                })
                .collect(Collectors.
                        groupingBy(
                                LawProjectResultTEntity::getLawTypeName,Collectors.counting())
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
    public List<JSONObject> getLawResultTimeStrStatistics(List<LawProjectResultTEntity> list,int yearStart,int yearEnd){
        //数据的总数
        long sumLawResultTimeStr = list.stream()
                .filter(lawProjectResultT -> {
                    String lawResultTimeStr = lawProjectResultT.getLawResultTimeStr();
                    return StringUtils.isBlank(lawResultTimeStr)?false:true;
                }).count();
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);

        Map<Object, Long> collectLawResultTime =  list.stream()
                .filter(lawProjectResultT -> {
                    String lawResultTimeStr = lawProjectResultT.getLawResultTimeStr();
                    return StringUtils.isBlank(lawResultTimeStr)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawProjectResultTEntity::getLawResultTimeStr,Collectors.counting())
                );
        List<JSONObject> jsonObjectLawResultTimeList = StatisticsUtils.getResultWithoutProportion(sumLawResultTimeStr,collectLawResultTime,yearsList);
        return jsonObjectLawResultTimeList;
    }

    /**
     * 按执法类别 统计近五年的年度数据
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getLawTypeNameWithYearsStatistics(List<LawProjectResultTEntity> list,int yearStart,int yearEnd){
        //按执法类别分组的数据,带百分比
        List standadList = rEnumUtil.getNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);
        long sumLawTypeName = list.stream()
                .filter(lawProjectResultT -> {
                    String lawTypeName = lawProjectResultT.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                }).count();
        Map<Object, Map<Object,Long>> collectLawTypeNameWithYears = list.stream()
                .filter(lawProjectResultT -> {
                    String lawTypeName = lawProjectResultT.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                })
                .collect(Collectors.
                        groupingBy(
                                LawProjectResultTEntity::getLawTypeName,Collectors.
                                        groupingBy(LawProjectResultTEntity::getLawResultTimeStr,Collectors.counting())
                        )
                );
        List<JSONObject> jsonObjectLawTypeNameWithYearsList = StatisticsUtils.getResultWithYears(sumLawTypeName,collectLawTypeNameWithYears,yearsList,standadList);
        return jsonObjectLawTypeNameWithYearsList;
    }
    /**
     * 获取按省份名称分组的数据
     * @param list 执法事项list
     * @returnl
     */
    @Override
    public List<JSONObject> getProvinceStatistics(List<LawProjectResultTEntity> list){
        //数据的总数
        long sumProvince = list.stream()
                .filter(lawProjectResultT -> {
                    String province = lawProjectResultT.getProvince();
                    return StringUtils.isBlank(province)?false:true;
                }).count();
        Map<Object, Long> collectProvince = list.stream()
                .filter(lawProjectResultT -> {
                    String province = lawProjectResultT.getProvince();
                    return StringUtils.isBlank(province)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawProjectResultTEntity::getProvince,Collectors.counting())
                );
        List<JSONObject> jsonObjectProvinceList = StatisticsUtils.getResultWithProportion(sumProvince,collectProvince);

        return jsonObjectProvinceList;

    }

    /**
     * 按省份 统计近五年的年度数据
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getProvinceWithYearsStatistics(List<LawProjectResultTEntity> list,int yearStart,int yearEnd){
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);
        //数据的总数
        long sumProvince = list.stream()
                .filter(lawProjectResultT -> {
                    String province = lawProjectResultT.getProvince();
                    String lawResultTimeStr = lawProjectResultT.getLawResultTimeStr();
                    return StringUtils.isBlank(province) || StringUtils.isBlank(lawResultTimeStr)?false:true;
                }).count();
        Map<Object, Map<Object,Long>> collectProvinceWithYears = list.stream()
                .filter(lawProjectResultT -> {
                    String province = lawProjectResultT.getProvince();
                    String lawResultTimeStr = lawProjectResultT.getLawResultTimeStr();
                    return StringUtils.isBlank(province) || StringUtils.isBlank(lawResultTimeStr)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawProjectResultTEntity::getProvince,Collectors.
                                        groupingBy(LawProjectResultTEntity::getLawResultTimeStr,Collectors.counting())
                        )
                );
        List<JSONObject> jsonObjectProvinceWithYearsList = StatisticsUtils.getResultWithYears(sumProvince,collectProvinceWithYears,yearsList);
        return jsonObjectProvinceWithYearsList;
    }


    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    @Override
    public List<LawProjectResultTEntity> getProcessedData(List<LawProjectResultTEntity> list){
        List provinceCodeAndNamelist = areaUtils.getProvince();
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        list.forEach(lawProjectResult->{
            //将执法结果时间 格式化到年 放入 lawResultTimeStr
            String lawResultTimeStr = DateUtils.format(lawProjectResult.getLawResultTime(),"yyyy");
            lawProjectResult.setLawResultTimeStr(lawResultTimeStr);
            //字典项转换
            //来源
            String provinceCode = lawProjectResult.getProvince();
            String nameOfProvince = areaUtils.convertDict("bmAreaUuid",provinceCode,"administrativeAreaName",
                    provinceCodeAndNamelist);
            lawProjectResult.setProvince(nameOfProvince);
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
    public List<LawProjectResultTEntity> downloadAll(){
        List<LawProjectResultTEntity> list = this.list(new QueryWrapper<LawProjectResultTEntity>()
                .ne("DEL_FLAG", "1"));
        list = this.praceCodeToName(list);
        return list;
    }

    /**
     * 批量下载
     * @return
     */
    @Override
    public List<LawProjectResultTEntity> downloadBatch(Map<String, Object> mapIds){
        //      直接获取map中存的数组map={"ids":[1,2,3]}
        List<Object> vlist = (List<Object>) mapIds.get("ids");
//      list 转化为数组
        Object[] ids = vlist.toArray(new Object[vlist.size()]);
//      Object... values代表Object[]
        List<LawProjectResultTEntity> list = this.list(new QueryWrapper<LawProjectResultTEntity>()
                .in(!mapIds.isEmpty(), "id", ids)
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
    private List<LawProjectResultTEntity> praceCodeToName(List<LawProjectResultTEntity> list){
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        List deptNameCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.DEPTNAME,CommonEnums.LawRoot.LAWROOT);
        List provinceCodeAndNamelist = areaUtils.getProvince();
        List lawMainCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWMAIN,CommonEnums.LawRoot.LAWROOT);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
            //字典项转换 name 转为 name 值
            //执法类别
            String lawTypeName = rEnumUtil.convertDict("code",list.get(i).getLawTypeId(),"name",lawTypeCodeAndNamelist);
            list.get(i).setLawTypeName(lawTypeName);
            //发布单位
            String deptName = rEnumUtil.convertDict("code",list.get(i).getDeptId(),"name",deptNameCodeAndNamelist);
            list.get(i).setDeptName(deptName);
            //来源
            String nameOfarea = areaUtils.convertDict("bmAreaUuid",list.get(i).getProvince(),"administrativeAreaName",
                    provinceCodeAndNamelist);
            list.get(i).setProvince(nameOfarea);
            //执法主体
            String lawMainId = list.get(i).getLawMain();
            String lawMainName = rEnumUtil.convertDict("code",lawMainId,"name",lawMainCodeAndNamelist);
            list.get(i).setLawMainStr(lawMainName);
        }
        return list;

    }




}