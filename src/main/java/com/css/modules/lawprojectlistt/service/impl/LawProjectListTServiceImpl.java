package com.css.modules.lawprojectlistt.service.impl;

import com.css.common.enums.CommonEnums;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.css.modules.lawprojectlistt.dao.LawProjectListTDao;
import com.css.modules.lawprojectlistt.entity.LawProjectListTEntity;
import com.css.modules.lawprojectlistt.service.LawProjectListTService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 执法事项清单
 *
 * @author
 * @email
 * @date 2019-11-14 14:02:12
 */
@Service("lawProjectListTService")
public class LawProjectListTServiceImpl extends ServiceImpl<LawProjectListTDao, LawProjectListTEntity> implements LawProjectListTService {
    @Autowired
    private REnumUtil rEnumUtil;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String lawMain = (String)params.get("lawMain");
        String lawTypeId = (String)params.get("lawTypeId");
        String pubStatus = (String)params.get("pubStatus");
        IPage<LawProjectListTEntity> page = this.page(
                new Query<LawProjectListTEntity>().getPage(params),
                new QueryWrapper<LawProjectListTEntity>()
                        .eq(StringUtils.isNotBlank(lawMain),"LAW_MAIN", lawMain)
                        .eq(StringUtils.isNotBlank(lawTypeId),"LAW_TYPE_ID", lawTypeId)
                        .eq(StringUtils.isNotBlank(pubStatus),"ISSUE_STATUS", pubStatus)
                        .ne("DEL_FLAG", "1")

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
    public void saveLawProjectListT(LawProjectListTEntity lawProjectListT){
        lawProjectListT.setId(null);
        lawProjectListT.setPubStatus("0");
        lawProjectListT.setPubDate(null);
        this.save(lawProjectListT);
    }
    @Override
    public void updateLawProjectListT(LawProjectListTEntity lawProjectListT){
        this.updateById(lawProjectListT);
    }

    @Override
    public String importLawProjectListT(MultipartFile file) throws Exception{
        List<LawProjectListTEntity> excelLawProjectListTlist = ExportExcelUtil.importExcel(file, 0, 1, LawProjectListTEntity.class);
        //用于字典项转换
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        List lawMainCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWMAIN,CommonEnums.LawRoot.LAWROOT);
        for (int i = 0; i < excelLawProjectListTlist.size(); i++) {
            excelLawProjectListTlist.get(i).setId(null);
            excelLawProjectListTlist.get(i).setPubStatus("0");
            excelLawProjectListTlist.get(i).setPubDate(null);
            //字典项转换 name 转换为 ID
            //执法类别
            String lawTypeId = rEnumUtil.convertDict("name",excelLawProjectListTlist.get(i).getLawTypeName(),"code",
                    lawTypeCodeAndNamelist);
            excelLawProjectListTlist.get(i).setLawTypeId(lawTypeId);
            //执法主体
            String lawMainId = rEnumUtil.convertDict("name",excelLawProjectListTlist.get(i).getLawMainStr(),"code",
                    lawMainCodeAndNamelist);
            excelLawProjectListTlist.get(i).setLawMain(lawMainId);
            ValidatorUtils.validateEntity(excelLawProjectListTlist.get(i), AddGroup.class);

        }
        if (excelLawProjectListTlist.size() != 0) {
            this.saveBatch(excelLawProjectListTlist);
        }
        return "导入成功";

    }

    @Override
    public List<JSONObject> getLawTypeNameStatistics(List<LawProjectListTEntity> list){
        List standadList = rEnumUtil.getNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        long sumLawTypeName = list.stream()
                .filter(lawProjectListT -> {
                    String lawTypeName = lawProjectListT.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                }).count();
        Map<Object, Long> collectLawTypeName    = list.stream()
                .filter(lawProjectListT -> {
                    String lawTypeName = lawProjectListT.getLawTypeName();
                    boolean has = standadList.contains(lawTypeName);
                    return has;
                })
                .collect(Collectors.
                        groupingBy(
                                LawProjectListTEntity::getLawTypeName,Collectors.counting())
                );
        List<JSONObject> jsonObjectLawTypeNameList = StatisticsUtils.getResultWithProportion(sumLawTypeName,collectLawTypeName,standadList);

        return jsonObjectLawTypeNameList;

    }
    @Override
    public List<JSONObject> getLawMainListStatistics(List<LawProjectListTEntity> list){
        //数据的总数
        long sumLawMain = list.stream()
                .filter(lawProjectListT -> {
                    String lawMainStr = lawProjectListT.getLawMainStr();
                    return StringUtils.isBlank(lawMainStr)?false:true;
                }).count();
        Map<Object, Long> collectLawMain = list.stream()
                .filter(lawProjectListT -> {
                    String lawMainStr = lawProjectListT.getLawMainStr();
                    return StringUtils.isBlank(lawMainStr)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawProjectListTEntity::getLawMainStr,Collectors.counting())
                );
        List<JSONObject> jsonObjectLawMainList = StatisticsUtils.getResultWithProportion(sumLawMain,collectLawMain);

        return jsonObjectLawMainList;
    }
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    @Override
    public List<LawProjectListTEntity> getProcessedData(List<LawProjectListTEntity> list){
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        List lawMainCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWMAIN,CommonEnums.LawRoot.LAWROOT);
        list.forEach(lawProjectListT->{
            //执法类别
            String lawTypeId = lawProjectListT.getLawTypeId();
            String lawTypeName = rEnumUtil.convertDict("code",lawTypeId,"name",lawTypeCodeAndNamelist);
            lawProjectListT.setLawTypeName(lawTypeName);
            //执法主体
            String lawMainId = lawProjectListT.getLawMain();
            String lawMainName = rEnumUtil.convertDict("code",lawMainId,"name",lawMainCodeAndNamelist);
            lawProjectListT.setLawMainStr(lawMainName);

        });
        return list;
    }
    /**
     * 下载全部
     * @return
     */
    @Override
    public List<LawProjectListTEntity> downloadAll(){
        List<LawProjectListTEntity> list = this.list(new QueryWrapper<LawProjectListTEntity>()
                .ne("DEL_FLAG", "1"));
        list = this.praceCodeToName(list);
        return list;

    }
    /**
     * 批量下载
     * @return
     */
    @Override
    public List<LawProjectListTEntity> downloadBatch(Map<String, Object> mapIds){
//        直接获取map中存的数组map={"ids":[1,2,3]}
        List<Object> vlist = (List<Object>) mapIds.get("ids");
//      list 转化为数组
        Object[] ids = vlist.toArray(new Object[vlist.size()]);
//      Object... values代表Object[]
        List<LawProjectListTEntity> list = this.list(new QueryWrapper<LawProjectListTEntity>()
                .in(!mapIds.isEmpty(), "id", ids)
                .ne("DEL_FLAG", "1"));
        for (int i = 0; i < list.size(); i++) {
            list = this.praceCodeToName(list);
        }
        return list;
    }
    /**
     * 下载EXCEL时 字典项转换
     *
     * @param list
     * @return
     */
    private List<LawProjectListTEntity> praceCodeToName(List<LawProjectListTEntity> list){
        List lawTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        List lawMainCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWMAIN,CommonEnums.LawRoot.LAWROOT);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
            //字典项转换 ID 转换为 name
            //执法类别
            String lawTypeName = rEnumUtil.convertDict("code",list.get(i).getLawTypeId(),"name",lawTypeCodeAndNamelist);
            list.get(i).setLawTypeName(lawTypeName);
            //执法主体
            String lawMainId = list.get(i).getLawMain();
            String lawMainName = rEnumUtil.convertDict("code",lawMainId,"name",lawMainCodeAndNamelist);
            list.get(i).setLawMainStr(lawMainName);
        }

        return list;

    }
}