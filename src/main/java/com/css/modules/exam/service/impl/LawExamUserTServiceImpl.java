package com.css.modules.exam.service.impl;

import com.css.common.utils.ExportExcelUtil;
import com.css.common.validator.ValidatorUtils;
import com.css.common.validator.group.AddGroup;
import com.css.modules.exam.entity.LawExamPlanTEntity;
import com.css.modules.exam.entity.LawExamUserTEntity;
import com.css.modules.exam.entity.LawExamUserTEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamUserTDao;
import com.css.modules.exam.entity.LawExamUserTEntity;
import com.css.modules.exam.service.LawExamUserTService;
import org.springframework.web.multipart.MultipartFile;


@Service("lawExamUserTService")
public class LawExamUserTServiceImpl extends ServiceImpl<LawExamUserTDao, LawExamUserTEntity> implements LawExamUserTService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");
        String testName = (String)params.get("testName");
        String isQualified = (String)params.get("isQualified");

        IPage<LawExamUserTEntity> page = this.page(
                new Query<LawExamUserTEntity>().getPage(params),
                new QueryWrapper<LawExamUserTEntity>()
                        .like(StringUtils.isNotBlank(name),"NAME", name)
                        .eq(StringUtils.isNotBlank(testName),"TEST_NAME", testName)
                        .eq(StringUtils.isNotBlank(isQualified),"IS_QUALIFIED", isQualified)
                        .ne("DEL_FLAG", "1")

        );
        return new PageUtils(page);
    }
    /**
     * 导入excel 同时 添加考试计划相关信息 和 试卷信息
     */
    @Override
    public String importLawExamUserTByExamPlan(MultipartFile file, LawExamPlanTEntity lawExamPlanT) throws Exception{
        List<LawExamUserTEntity> excelLawExamUserTlist = ExportExcelUtil.importExcel(file, 0, 1, LawExamUserTEntity.class);

        for (int i = 0; i < excelLawExamUserTlist.size(); i++) {
            //添加 考试计划信息
            excelLawExamUserTlist.get(i).setPlanId(lawExamPlanT.getId());
            excelLawExamUserTlist.get(i).setExamTime(lawExamPlanT.getExamTime());
            excelLawExamUserTlist.get(i).setExamStartTime(lawExamPlanT.getExamStartTime());
            excelLawExamUserTlist.get(i).setExamEndTime(lawExamPlanT.getExamEndTime());
            //添加 试卷信息
            excelLawExamUserTlist.get(i).setTestId(lawExamPlanT.getTestId());
            excelLawExamUserTlist.get(i).setTestName(lawExamPlanT.getTestName());
            //默认状态
            excelLawExamUserTlist.get(i).setId(null);
            excelLawExamUserTlist.get(i).setDelFlag("0");
        }
        if (excelLawExamUserTlist.size() != 0) {
            this.saveBatch(excelLawExamUserTlist);
        }
        return "导入成功";

    }

    @Override
    public String importLawExamUserT(MultipartFile file) throws Exception{
        List<LawExamUserTEntity> excelLawExamUserTlist = ExportExcelUtil.importExcel(file, 0, 1, LawExamUserTEntity.class);
        for (int i = 0; i < excelLawExamUserTlist.size(); i++) {
            excelLawExamUserTlist.get(i).setId(null);
            excelLawExamUserTlist.get(i).setDelFlag("0");
            ValidatorUtils.validateEntity(excelLawExamUserTlist.get(i), AddGroup.class);
        }
        if (excelLawExamUserTlist.size() != 0) {
            this.saveBatch(excelLawExamUserTlist);
        }
        return "导入成功";

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