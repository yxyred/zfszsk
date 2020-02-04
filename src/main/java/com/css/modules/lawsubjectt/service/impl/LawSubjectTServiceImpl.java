package com.css.modules.lawsubjectt.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.modules.lawsubjectt.dao.LawSubjectTDao;
import com.css.modules.lawsubjectt.entity.LawSubjectTEntity;
import com.css.modules.lawsubjectt.entity.RemoteSubjectEntity;
import com.css.modules.lawsubjectt.service.LawSubjectTService;
import com.css.modules.remote.util.HttpRequestUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service("lawSubjectTService")
public class LawSubjectTServiceImpl extends ServiceImpl<LawSubjectTDao, LawSubjectTEntity> implements LawSubjectTService {
    @Autowired
    private AreaUtils areaUtils;
    @Autowired
    private HttpRequestUtils httpRequestUtils;
    @Autowired
    private SsoUrlUtils ssoUrlUtils;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String uniscid = (String)params.get("uniscid");
        String subjectName = (String)params.get("subjectName");
        IPage<LawSubjectTEntity> page = this.page(
                new Query<LawSubjectTEntity>().getPage(params),
                new QueryWrapper<LawSubjectTEntity>()
                .like(StringUtils.isNotBlank(uniscid),"UNISCID",uniscid)
                .like(StringUtils.isNotBlank(subjectName),"SUBJECT_NAME",subjectName)
                        .ne("DEL_FLAG", "1")
        );

        return new PageUtils(page);
    }
    @Override
    public String importLawSubjectT(MultipartFile file) throws Exception {
        List<LawSubjectTEntity> excelLawSubjectTlist = ExportExcelUtil.importExcel(file, 0, 1, LawSubjectTEntity.class);
        List provinceCodeAndNamelist = areaUtils.getProvince();
        for (int i = 0; i < excelLawSubjectTlist.size(); i++) {
            excelLawSubjectTlist.get(i).setId(null);
            //来源
            String codeOfarea = areaUtils.convertDict("administrativeAreaName",excelLawSubjectTlist.get(i).getProvince(),"bmAreaUuid",
                    provinceCodeAndNamelist);
            excelLawSubjectTlist.get(i).setProvince(codeOfarea);
            ValidatorUtils.validateEntity(excelLawSubjectTlist.get(i), AddGroup.class);
        }
        if (excelLawSubjectTlist.size() != 0) {
            this.saveBatch(excelLawSubjectTlist);
        }
        return "导入成功";
    }
    /**
     * 下载全部
     * @return
     */
    @Override
    public List<LawSubjectTEntity> downloadAll(){
        List<LawSubjectTEntity> list = this.list(new QueryWrapper<LawSubjectTEntity>()
                .ne("DEL_FLAG", "1"));
        list = this.praceCodeToName(list);
        return list;
    }

    /**
     * 批量下载
     * @return
     */
    @Override
    public List<LawSubjectTEntity> downloadBatch(Map<String, Object> mapIds){
//      直接获取map中存的数组map={"ids":[1,2,3]}
        List<Object> vlist = (List<Object>) mapIds.get("ids");
//      list 转化为数组
        Object[] ids = vlist.toArray(new Object[vlist.size()]);
//      Object... values代表Object[]
        List<LawSubjectTEntity> list = this.list(new QueryWrapper<LawSubjectTEntity>()
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
    private List<LawSubjectTEntity> praceCodeToName(List<LawSubjectTEntity> list){
        List provinceCodeAndNamelist = areaUtils.getProvince();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
            //字典项转换 name 转为 name 值
            //来源
            String nameOfarea = areaUtils.convertDict("bmAreaUuid",list.get(i).getProvince(),"administrativeAreaName",
                    provinceCodeAndNamelist);
            list.get(i).setProvince(nameOfarea);
        }
        return list;

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
    public void saveLawSubjectT(LawSubjectTEntity lawSubjectTEntity){
        lawSubjectTEntity.setDelFlag("0");
        lawSubjectTEntity.setId(null);
//        lawSubjectTEntity.setPubStatus("0");
        this.save(lawSubjectTEntity);
    }
    @Override
    public void updateLawSubjectT(LawSubjectTEntity lawSubjectTEntity){
        this.updateById(lawSubjectTEntity);
    }

    @Override
    public LawSubjectTEntity getRemoteSubjectByUniscid(String uniscid) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("enterpriseUuid", uniscid);
        String paramStr = JSON.toJSONString(paramMap);
        com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) httpRequestUtils.sendHttpRequestForm(ssoUrlUtils.getQueryCompany(), paramStr, null, HttpMethod.POST);
        if(null == jsonObject){
            return null;
        }
        com.alibaba.fastjson.JSONObject params = null;
        if((boolean)jsonObject.get("success")){
            params = (com.alibaba.fastjson.JSONObject) jsonObject.get("params");
        }else{
            return null;
        }
        if(null == params){
            return null;
        }
        RemoteSubjectEntity remoteSubjectEntity = JSON.toJavaObject(params, RemoteSubjectEntity.class);
        if(null == remoteSubjectEntity){
            return null;
        }

        LawSubjectTEntity lawSubjectTEntity = new LawSubjectTEntity();
        //获取省
        List provinceCodeAndNamelist = areaUtils.getProvince();
        String manufacturingProvinceCode = remoteSubjectEntity.getManufacturingProvinceCode();
        String codeOfProvince = areaUtils.convertDict("bmAreaUuid",manufacturingProvinceCode,"administrativeAreaName",
                provinceCodeAndNamelist);
        //获取市，通过市code获取name待完成
        List city = areaUtils.getCity(manufacturingProvinceCode);

        lawSubjectTEntity.setProvince(codeOfProvince);
        lawSubjectTEntity.setSubjectName(remoteSubjectEntity.getEnterpriseName());
        lawSubjectTEntity.setBuzSco(remoteSubjectEntity.getEnterpriserange());
        lawSubjectTEntity.setUniscid(uniscid);
        lawSubjectTEntity.setSubjectId(remoteSubjectEntity.getEnterpriseUuid());
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        try {
            parse = format.parse(remoteSubjectEntity.getCreatedate());
        } catch (ParseException e) {
        }
        lawSubjectTEntity.setEnterpriseCreateDate(parse);
        return lawSubjectTEntity;
    }

    /**
     * 按省份 统计近五年的年度数据
     * @param list
     * @returnl
     */
    @Override
    public List<JSONObject> getProvinceWithYearsStatistics(List<LawSubjectTEntity> list,int yearStart,int yearEnd){
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);
        //数据的总数
        long sumProvince = list.stream()
                .filter(lawSubjectTEntity -> {
                    String province = lawSubjectTEntity.getProvince();
                    String enterpriseCreateDateStr = lawSubjectTEntity.getEnterpriseCreateDateStr();
                    return StringUtils.isBlank(province) || StringUtils.isBlank(enterpriseCreateDateStr) ?false:true;
                }).count();
        Map<Object, Map<Object,Long>> collectProvinceWithYears = list.stream()
                .filter(lawSubjectTEntity -> {
                    String province = lawSubjectTEntity.getProvince();
                    String enterpriseCreateDateStr = lawSubjectTEntity.getEnterpriseCreateDateStr();
                    return StringUtils.isBlank(province) || StringUtils.isBlank(enterpriseCreateDateStr) ?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                LawSubjectTEntity::getProvince,Collectors.
                                        groupingBy(LawSubjectTEntity::getEnterpriseCreateDateStr,Collectors.counting())
                        )
                );
        List<JSONObject> jsonObjectProvinceWithYearsList = StatisticsUtils.getResultWithYears(sumProvince,collectProvinceWithYears,yearsList);
        return jsonObjectProvinceWithYearsList;
    }
    /**
     * 按省份 统计近五年的年度数据
     * @param
     * @returnl
     */
    @Override
    public List<JSONObject> getProvinceIncreaseWithYearsStatistics(List<JSONObject> jsonObjecProvinceWithYearsList,int yearStart,int yearEnd){
        List<Integer> yearsList = DateUtils.getYears(yearStart,yearEnd);
        for (int i = 0; i < jsonObjecProvinceWithYearsList.size(); i++) {
            JSONObject jsonObject = jsonObjecProvinceWithYearsList.get(i);
            String str = jsonObject.get("count").toString().replaceAll(" ","");
            List list = Arrays.asList(str.substring(1,str.length()-1).split(","));
            List newlist = new ArrayList();
            if(0 < list.size()){
                for (int j = 1; j < list.size(); j++) {
                    int preNum = Integer.valueOf(list.get(j-1).toString());
                    int curNum = Integer.valueOf(list.get(j).toString());
                    int num = curNum - preNum;
                    newlist.add(num);
                }
            }
            jsonObject.put("count",newlist);
            jsonObject.put("years", yearsList);
        }
        return jsonObjecProvinceWithYearsList;
    }
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    @Override
    public List<LawSubjectTEntity> getProcessedData(List<LawSubjectTEntity> list){
        List provinceCodeAndNamelist = areaUtils.getProvince();
        list.forEach(lawSubjectTEntity->{
            //将执法结果时间 格式化到年 放入 lawResultTimeStr
            String enterpriseCreateDateStr = DateUtils.format(lawSubjectTEntity.getEnterpriseCreateDate(),"yyyy");
            lawSubjectTEntity.setEnterpriseCreateDateStr(enterpriseCreateDateStr);
            //字典项转换
            //来源
            String provinceCode = lawSubjectTEntity.getProvince();
            String nameOfProvince = areaUtils.convertDict("bmAreaUuid",provinceCode,"administrativeAreaName",
                    provinceCodeAndNamelist);
            lawSubjectTEntity.setProvince(nameOfProvince);
        });
        return list;
    }

}