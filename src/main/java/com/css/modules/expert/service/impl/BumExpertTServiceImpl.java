package com.css.modules.expert.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.css.common.enums.CommonEnums;
import com.css.common.exception.CvaException;
import com.css.common.utils.*;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.modules.expert.dao.BumExpertTDao;
import com.css.modules.expert.entity.BumExpertAndExperienceTEntity;
import com.css.modules.expert.entity.BumExpertExperienceTEntity;
import com.css.modules.expert.entity.BumExpertTEntity;
import com.css.modules.expert.entity.BumExpertTExcelEntity;
import com.css.modules.expert.service.BumExpertExperienceTService;
import com.css.modules.expert.service.BumExpertTService;
import com.css.modules.lawprojectresultt.entity.LawProjectResultTEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service("bumExpertTService")
public class BumExpertTServiceImpl extends ServiceImpl<BumExpertTDao, BumExpertTEntity> implements BumExpertTService {

    @Autowired
    private BumExpertExperienceTService bumExpertExperienceTService;
    @Autowired
    private REnumUtil rEnumUtil;
    @Autowired
    private AreaUtils areaUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        BumExpertTEntity paramEntity = new BumExpertTEntity();
        String name = (String) params.get("name");
        String unitName = (String) params.get("unitName");
        String province = (String) params.get("province");
        String city = (String) params.get("city");
        if(null != name && !"".equals(name)){
            paramEntity.setName(name);
        }
        if(null != unitName && !"".equals(unitName)){
            paramEntity.setUnitName(unitName);
        }
        if(null != province && !"".equals(province)){
            paramEntity.setProvince(province);
        }
        if(null != city && !"".equals(city)){
            paramEntity.setCity(city);
        }
        IPage<BumExpertTEntity> page = this.page(
                new Query<BumExpertTEntity>().getPage(params),
                new QueryWrapper<BumExpertTEntity>(paramEntity)
        );
        List<BumExpertTEntity> BumExpertTEntitys = page.getRecords();
        List<BumExpertAndExperienceTEntity> bumExpertAndExperienceTEntities = null;
        if(null != BumExpertTEntitys && BumExpertTEntitys.size()>0){
            bumExpertAndExperienceTEntities = new ArrayList<>();
            for(BumExpertTEntity bumExpertTEntity: BumExpertTEntitys){
                QueryWrapper<BumExpertExperienceTEntity> queryWrapper = new QueryWrapper<>();
                BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity = new BumExpertAndExperienceTEntity();
                BeanUtil.copyProperties(bumExpertTEntity, bumExpertAndExperienceTEntity);
                queryWrapper.lambda().eq(BumExpertExperienceTEntity::getExpertId, bumExpertTEntity.getId());
                bumExpertAndExperienceTEntity.setBumExpertExperienceTEntitys(bumExpertExperienceTService.list(queryWrapper));
                bumExpertAndExperienceTEntities.add(bumExpertAndExperienceTEntity);
            }
        }

        return new PageUtils(bumExpertAndExperienceTEntities, (int)page.getTotal(), (int)page.getSize(), (int)page.getCurrent());
    }




    @Transactional
    @Override
    public boolean saveExpert(BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity) {
        if(null == bumExpertAndExperienceTEntity){
            return false;
        }
        BumExpertTEntity bumExpertTEntity = new BumExpertTEntity();
        BeanUtil.copyProperties(bumExpertAndExperienceTEntity, bumExpertTEntity);
        //主表
        this.baseMapper.insert(bumExpertTEntity);
        List<BumExpertExperienceTEntity> bumExpertExperienceTEntities = bumExpertAndExperienceTEntity.getBumExpertExperienceTEntitys();
        List<BumExpertExperienceTEntity> bumExpertExperienceTEntitieTemps = new ArrayList<>();
        for (BumExpertExperienceTEntity bumExpertExperienceTEntity: bumExpertExperienceTEntities){
            bumExpertExperienceTEntity.setExpertId(bumExpertTEntity.getId());
            bumExpertExperienceTEntitieTemps.add(bumExpertExperienceTEntity);
        }
        //执法经历表
        return bumExpertExperienceTService.saveBatch(bumExpertExperienceTEntitieTemps);

    }

    @Override
    public BumExpertAndExperienceTEntity selectById(String id) {
        BumExpertTEntity bumExpertTEntity = this.getById(id);
        BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity = null;
        if(null != bumExpertTEntity){
            QueryWrapper<BumExpertExperienceTEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(BumExpertExperienceTEntity::getExpertId, bumExpertTEntity.getId());
            bumExpertAndExperienceTEntity = new BumExpertAndExperienceTEntity();
            BeanUtil.copyProperties(bumExpertTEntity, bumExpertAndExperienceTEntity);
            bumExpertAndExperienceTEntity.setBumExpertExperienceTEntitys(bumExpertExperienceTService.list(queryWrapper));
        }
        return bumExpertAndExperienceTEntity;
    }

    @Transactional
    @Override
    public boolean updateById(BumExpertAndExperienceTEntity bumExpertAndExperienceTEntity) {
        BumExpertTEntity bumExpertTEntity = new BumExpertTEntity();
        List<BumExpertExperienceTEntity> bumExpertExperienceTEntitys = bumExpertAndExperienceTEntity.getBumExpertExperienceTEntitys();
        BeanUtil.copyProperties(bumExpertAndExperienceTEntity, bumExpertTEntity);
        boolean b1 = this.updateById(bumExpertTEntity);
        if(null != bumExpertExperienceTEntitys && bumExpertExperienceTEntitys.size()>0 && b1){
            return bumExpertExperienceTService.updateBatchById(bumExpertExperienceTEntitys);
        }
        return false;
    }

    @Transactional
    @Override
    public void importBumExpertT(MultipartFile file){
        //省编码
        List provinceCodeAndNamelist = areaUtils.getProvince();
        //技术专长
        List expertiseList = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.EXPERTISE,CommonEnums.LawRoot.LAWROOT);
        //执法类别（辅法类别）
        List lawTypeNameList = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);
        //性别
        List genderList = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.GENDER,CommonEnums.LawRoot.LAWROOT);

        List<BumExpertTExcelEntity> bumExpertTExcelEntities = null;
        try {
            bumExpertTExcelEntities = ExportExcelUtil.importExcel(file, 0, 1, BumExpertTExcelEntity.class);
        } catch (Exception e) {
            throw new CvaException("导入失败", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        List<BumExpertExperienceTEntity> bumExpertExperienceTEntities = new ArrayList<>();
        BumExpertExperienceTEntity bumExpertExperienceTEntity = null;

        Map<String, BumExpertTExcelEntity> map = new HashMap<>();
        if(null != bumExpertTExcelEntities && bumExpertTExcelEntities.size() > 0){
            for(BumExpertTExcelEntity bumExpertTExcelEntity: bumExpertTExcelEntities){
                //去重
                map.put(bumExpertTExcelEntity.getCert(), bumExpertTExcelEntity);
            }
            List<BumExpertTEntity> bumExpertTEntities = new ArrayList<>();
            BumExpertTEntity bumExpertTEntity = null;
            for(String key: map.keySet()){
                bumExpertTEntity = new BumExpertTEntity();
                BumExpertTExcelEntity bumExpertTExcelEntity = map.get(key);
                //设置省代码
                String codeOfarea = areaUtils.convertDict("administrativeAreaName",bumExpertTExcelEntity.getProvince(),"bmAreaUuid",
                        provinceCodeAndNamelist);
                bumExpertTExcelEntity.setProvince(codeOfarea);
                //技术专长
                String expertiseCode = rEnumUtil.convertDict("name",bumExpertTExcelEntity.getExpertise(),"code",
                        expertiseList);
                bumExpertTExcelEntity.setExpertise(expertiseCode);
                //性别
                String genderCode = rEnumUtil.convertDict("name",bumExpertTExcelEntity.getGender(),"code",
                        genderList);
                bumExpertTExcelEntity.setGender(genderCode);

                bumExpertTExcelEntity.setId(null);
                BeanUtil.copyProperties(bumExpertTExcelEntity, bumExpertTEntity);
                ValidatorUtils.validateEntity(bumExpertTEntity, AddGroup.class);
                bumExpertTEntities.add(bumExpertTEntity);
            }
            //保存专家基本信息
            this.saveBatch(bumExpertTEntities);
            //提取执法经历
            for(int i=0; i<bumExpertTExcelEntities.size(); i++){
                boolean flag = false;
                for(int j=0; j<bumExpertTEntities.size(); j++){
                    if(bumExpertTExcelEntities.get(i).getCert().equals(bumExpertTEntities.get(j).getCert())){
                        bumExpertExperienceTEntity = new BumExpertExperienceTEntity();
                        //执法类别
                        String experienceTypeCode = rEnumUtil.convertDict("name",bumExpertTExcelEntities.get(i).getExperienceType(),"code",
                                lawTypeNameList);
                        bumExpertExperienceTEntity.setExperienceType(experienceTypeCode);
                        bumExpertExperienceTEntity.setExperience(bumExpertTExcelEntities.get(i).getExperience());
                        bumExpertExperienceTEntity.setExpertId(bumExpertTEntities.get(j).getId());
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    ValidatorUtils.validateEntity(bumExpertExperienceTEntity, AddGroup.class);
                    bumExpertExperienceTEntities.add(bumExpertExperienceTEntity);
                }

            }
            bumExpertExperienceTService.saveBatch(bumExpertExperienceTEntities);
        }
    }

    @Override
    public List<BumExpertTExcelEntity> exportByIds(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("list", map.get("ids"));
        List<BumExpertTExcelEntity> bumExpertTExcelEntities = baseMapper.exportByIds(paramMap);
        //转换
        bumExpertTExcelEntities = parseCodeToName(bumExpertTExcelEntities);
        return bumExpertTExcelEntities;
    }

    /**
     * 下载EXCEL时 字典项转换
     *
     * @param list
     * @return
     */
    private List<BumExpertTExcelEntity> parseCodeToName(List<BumExpertTExcelEntity> list){

        List provinceCodeAndNameList = areaUtils.getProvince();
        //性别
        List gender = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.GENDER,CommonEnums.LawRoot.LAWROOT);

        //技能专长
        List expertiseCodeAndNameList = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.EXPERTISE,CommonEnums.LawRoot.LAWROOT);
        //辅法类别
        List lawTypeNAMECodeAndNameList = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.LAWTYPENAME,CommonEnums.LawRoot.LAWROOT);


        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(Integer.toString(i + 1));
            //字典项转换 name 转为 name 值
            //性别
            String genderName = rEnumUtil.convertDict("code",list.get(i).getGender(),"name",gender);
            list.get(i).setGender(genderName);
            //来源
            String nameOfarea = areaUtils.convertDict("bmAreaUuid",list.get(i).getProvince(),"administrativeAreaName",
                    provinceCodeAndNameList);
            list.get(i).setProvince(nameOfarea);



            //技能专长
            String expertise = rEnumUtil.convertDict("code",list.get(i).getExpertise(),"name",expertiseCodeAndNameList);
            list.get(i).setExpertise(expertise);

            //辅法类别
            String experienceType = rEnumUtil.convertDict("code",list.get(i).getExperienceType(),"name",lawTypeNAMECodeAndNameList);
            list.get(i).setExpertise(experienceType);

        }
        return list;
    }


    @Override
    public List<JSONObject> getExpertiseStatistics(List<BumExpertTEntity> list){
        List standadList = rEnumUtil.getNameList(
                CommonEnums.LawDict.EXPERTISE,CommonEnums.LawRoot.LAWROOT);

        long sumExpertise = list.stream()
                .filter(bumExpertT -> {
                    String expertise = bumExpertT.getExpertise();
                    boolean has = standadList.contains(expertise);
                    return has;
                }).count();
        Map<Object, Long> collectExpertise    = list.stream()
                .filter(bumExpertT -> {
                    String expertise = bumExpertT.getExpertise();
                    boolean has = standadList.contains(expertise);
                    return has;
                })
                .collect(Collectors.
                        groupingBy(
                                BumExpertTEntity::getExpertise,Collectors.counting())
                );
        List<JSONObject> jsonObjectExpertiseList = StatisticsUtils.getResultWithProportion(sumExpertise,collectExpertise,standadList);

        return jsonObjectExpertiseList;

    }

    @Override
    public List<JSONObject> getUnitTypeNameStatistics(List<BumExpertTEntity> list){
        List standadList = rEnumUtil.getNameList(
                CommonEnums.LawDict.UNITTYPENAME,CommonEnums.LawRoot.LAWROOT);

        long sumUnitTypeName = list.stream()
                .filter(bumExpertT -> {
                    String unitTypeName = bumExpertT.getUnitTypeName();
                    boolean has = standadList.contains(unitTypeName);
                    return has;
                }).count();
        Map<Object, Long> collectUnitTypeName    = list.stream()
                .filter(bumExpertT -> {
                    String unitTypeName = bumExpertT.getUnitTypeName();
                    boolean has = standadList.contains(unitTypeName);
                    return has;
                })
                .collect(Collectors.
                        groupingBy(
                                BumExpertTEntity::getUnitTypeName,Collectors.counting())
                );
        List<JSONObject> jsonObjectUnitTypeNameList = StatisticsUtils.getResultWithProportion(sumUnitTypeName,collectUnitTypeName,standadList);

        return jsonObjectUnitTypeNameList;

    }

    @Override
    public List<JSONObject> getProvinceStatistics(List<BumExpertTEntity> list){
        //数据的总数
        long sumArea = list.stream()
                .filter(bumExpertT -> {
                    String province = bumExpertT.getProvince();
                    return StringUtils.isBlank(province)?false:true;
                }).count();
        Map<Object, Long> collectArea = list.stream()
                .filter(bumExpertT -> {
                    String province = bumExpertT.getProvince();
                    return StringUtils.isBlank(province)?false:true;
                })
                .collect(Collectors.
                        groupingBy(
                                BumExpertTEntity::getProvince,Collectors.counting())
                );
        List<JSONObject> jsonObjectAreaList = StatisticsUtils.getResultWithProportion(sumArea,collectArea);

        return jsonObjectAreaList;

    }

    /**
     * 专家随机抽取
     * @param params
     * @return
     */
    @Transactional
    @Override
    public PageUtils randomExtraction(Map<String, Object> params) {
        if(null == params.get("nums") || "".equals(params.get("nums"))){
            throw new CvaException("抽取人数不能为空");
        }
        String numStrs = String.valueOf(params.get("nums"));
        int nums = Integer.parseInt(numStrs);
        PageUtils page = this.selectPage(params, nums);
        //数据库记录数
        Integer count = this.baseMapper.selectCount(new QueryWrapper<BumExpertTEntity>());
        List<BumExpertTExcelEntity> bumExpertTExcelEntities = (List<BumExpertTExcelEntity>)page.getList();
        List<BumExpertTExcelEntity> bumExpertTExcelEntityResults = new ArrayList<>();
        if(nums > count){
            throw new CvaException("抽取人数大于总人数");
        }
        Random random = new Random();
        int size = bumExpertTExcelEntities.size();
        if(nums > size){
            throw new CvaException("抽取人数大于筛选人数");
        }
        if(0 == size){
            return page;
        }
        Set<Integer> totals = new HashSet<Integer>();
        while (totals.size() < nums) {
            //随机再集合里取出元素，添加到新哈希集合
            totals.add(random.nextInt(size));
        }
        Iterator iterator = totals.iterator();
        while (iterator.hasNext()) {
            int next = (int) iterator.next();
            bumExpertTExcelEntityResults.add(bumExpertTExcelEntities.get(next));
        }
        //更新筛选状态
        if(null != bumExpertTExcelEntityResults && bumExpertTExcelEntityResults.size() > 0){
            for(BumExpertTExcelEntity bumExpertTExcelEntity: bumExpertTExcelEntityResults){
                BumExpertTEntity bumExpertTEntity = this.baseMapper.selectById(bumExpertTExcelEntity.getId());
                bumExpertTEntity.setStatus("1");
                this.baseMapper.updateById(bumExpertTEntity);
            }
        }

        List<BumExpertTExcelEntity> bumExpertTExcelEntityResultList = new ArrayList<>();
        for(BumExpertTExcelEntity bumExpertTExcelEntity : bumExpertTExcelEntityResults){
            List<BumExpertExperienceTEntity> bumExpertExperienceTEntities = bumExpertExperienceTService.list(new QueryWrapper<BumExpertExperienceTEntity>().eq("expert_id", bumExpertTExcelEntity.getId()));
            if(null != bumExpertExperienceTEntities && bumExpertExperienceTEntities.size() > 0){
                StringBuilder sb = new StringBuilder();
                for(BumExpertExperienceTEntity bumExpertExperienceTEntity: bumExpertExperienceTEntities){
                    sb.append(bumExpertExperienceTEntity.getExperience());
                    sb.append(",");
                }
                bumExpertTExcelEntity.setExperience(sb.toString());
            }
            bumExpertTExcelEntityResultList.add(bumExpertTExcelEntity);
        }
        page = new PageUtils(bumExpertTExcelEntityResultList, bumExpertTExcelEntityResultList.size(), (int)page.getPageSize(), (int)page.getCurrPage());
        return page;
    }

    @Transactional
    @Override
    public PageUtils selectPage(Map<String, Object> params, int nums) {
        BumExpertTExcelEntity bumExpertTExcelEntity = new BumExpertTExcelEntity();
        String expertises = (String)params.get("expertise");
        List<String> expertiseList = null;
        List<String> experienceTypeList = null;
        if(null != expertises && !"".equals(expertises)){
            expertiseList = Arrays.asList(expertises.split(","));
        }

        String experienceTypes = (String)params.get("experienceType");
        if(null != experienceTypes && !"".equals(experienceTypes)){
            experienceTypeList = Arrays.asList(experienceTypes.split(","));
        }
        String province = (String)params.get("province");
        String city = (String)params.get("city");

        bumExpertTExcelEntity.setExpertises(expertiseList);
        bumExpertTExcelEntity.setExperienceTypes(experienceTypeList);
        bumExpertTExcelEntity.setProvince(province);
        bumExpertTExcelEntity.setCity(city);
        IPage<BumExpertTExcelEntity> page = new Query<BumExpertTExcelEntity>().getPage(params);
        long size = page.getSize();
        if(size < nums){
            page.setSize(nums);
        }
        IPage<BumExpertTExcelEntity> bumExpertTExcelEntityIPage = this.baseMapper.selectMyPage(page,
                new QueryWrapper<>(bumExpertTExcelEntity));
        return new PageUtils(bumExpertTExcelEntityIPage.getRecords(), (int)bumExpertTExcelEntityIPage.getTotal(),
                (int)size, (int)bumExpertTExcelEntityIPage.getCurrent());
    }
    /**
     * 统计前清洗数据
     * @param list
     * @returnl
     */
    @Override
    public List<BumExpertTEntity> getProcessedData(List<BumExpertTEntity> list){
        List provinceCodeAndNamelist = areaUtils.getProvince();
        List expertiseCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.EXPERTISE,CommonEnums.LawRoot.LAWROOT);
        List unitTypeCodeAndNamelist = rEnumUtil.getCodeAndNameList(
                CommonEnums.LawDict.UNITTYPENAME,CommonEnums.LawRoot.LAWROOT);
        list.forEach(bumExpertTEntity->{
            //字典项转换
            //来源
            String provinceCode = bumExpertTEntity.getProvince();
            String provinceName = areaUtils.convertDict("bmAreaUuid",provinceCode,"administrativeAreaName",
                    provinceCodeAndNamelist);
            bumExpertTEntity.setProvince(provinceName);
            //技术专长
            String expertiseCode = bumExpertTEntity.getExpertise();
            String expertiseName = rEnumUtil.convertDict("code",expertiseCode,"name",
                    expertiseCodeAndNamelist);
            bumExpertTEntity.setExpertise(expertiseName);
            //工作单位类别
            String unitTypeCode = bumExpertTEntity.getUnitType();
            String unitTypeName = rEnumUtil.convertDict("code",unitTypeCode,"name",
                    unitTypeCodeAndNamelist);
            bumExpertTEntity.setUnitTypeName(unitTypeName);

        });
        return list;
    }



}