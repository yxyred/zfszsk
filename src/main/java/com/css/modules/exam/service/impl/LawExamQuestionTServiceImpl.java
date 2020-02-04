package com.css.modules.exam.service.impl;

import com.css.common.enums.CommonEnums;
import com.css.common.utils.ExportExcelUtil;
import com.css.common.utils.REnumUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamQuestionTDao;
import com.css.modules.exam.entity.LawExamQuestionTEntity;
import com.css.modules.exam.service.LawExamQuestionTService;
import org.springframework.web.multipart.MultipartFile;


@Service("lawExamQuestionTService")
public class LawExamQuestionTServiceImpl extends ServiceImpl<LawExamQuestionTDao, LawExamQuestionTEntity> implements LawExamQuestionTService {

    @Autowired
    private REnumUtil rEnumUtil;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        List examQuestionType = rEnumUtil.getExamQuestionTypeList(
                CommonEnums.LawDict.EXAMQUESTIONTYPE,CommonEnums.LawRoot.LAWROOT);
        String questionTypeId = (String)params.get("questionTypeId");
        IPage<LawExamQuestionTEntity> page = this.page(
                new Query<LawExamQuestionTEntity>().getPage(params),
                new QueryWrapper<LawExamQuestionTEntity>()
                        .eq(StringUtils.isNotBlank(questionTypeId),"QUESTION_TYPE_ID", questionTypeId)
                        .ne("DEL_FLAG", "1")
        );
        List<LawExamQuestionTEntity> list = page.getRecords();
        if (null != list && 0 != list.size()) {
            for (int i = 0; i < list.size() ; i++) {
                int index = -1;
                boolean has = false;
                for (int j = 0; j < examQuestionType.size() ; j++) {
                    Map map = (Map)examQuestionType.get(j);
                    if(StringUtils.isNotBlank(list.get(i).getQuestionTypeId())
                            && map.get("code").toString().equals(list.get(i).getQuestionTypeId())){
                        index = j;
                        has = true;
                        break;
                    }
                }
                if(index != -1 && has == true){
                    Map map = (Map)examQuestionType.get(index);
                    list.get(i).setExamScore(map.get("examScore").toString());
                    list.get(i).setQuestionTypeName(map.get("name").toString());
                }
            }
            page.setRecords(list);
        }

        return new PageUtils(page);
    }

    @Override
    public String importLawExamQuestionT(MultipartFile file) throws Exception{
        List examQuestionType = rEnumUtil.getExamQuestionTypeList(
                CommonEnums.LawDict.EXAMQUESTIONTYPE,CommonEnums.LawRoot.LAWROOT);
        List<LawExamQuestionTEntity> excelLawProjectResultTlist = ExportExcelUtil.importExcel(file, 0, 1, LawExamQuestionTEntity.class);
        for (int i = 0; i < excelLawProjectResultTlist.size(); i++) {
            int index = -1;
            boolean has = false;
            for (int j = 0; j < examQuestionType.size() ; j++) {
                Map map = (Map)examQuestionType.get(j);
                if(StringUtils.isNotBlank(excelLawProjectResultTlist.get(i).getQuestionTypeName())
                        && map.get("name").toString().equals(excelLawProjectResultTlist.get(i).getQuestionTypeName())){
                    index = j;
                    has = true;
                    break;
                }
            }
            if(index != -1 && has == true){
                Map map = (Map)examQuestionType.get(index);
                excelLawProjectResultTlist.get(i).setQuestionTypeId(map.get("code").toString());
            }

            excelLawProjectResultTlist.get(i).setId(null);
            excelLawProjectResultTlist.get(i).setImportTime(new Date());
            excelLawProjectResultTlist.get(i).setDelFlag("0");

        }
        if (excelLawProjectResultTlist.size() != 0) {
            this.saveBatch(excelLawProjectResultTlist);
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