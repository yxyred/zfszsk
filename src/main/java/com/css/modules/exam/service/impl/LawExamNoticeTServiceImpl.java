package com.css.modules.exam.service.impl;

import com.css.common.utils.ShiroUtils;
import com.css.modules.exam.entity.LawExamUserTEntity;
import com.css.modules.exam.service.LawExamUserTService;
import com.css.modules.sys.entity.SysUserTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;

import com.css.modules.exam.dao.LawExamNoticeTDao;
import com.css.modules.exam.entity.LawExamNoticeTEntity;
import com.css.modules.exam.service.LawExamNoticeTService;


@Service("lawExamNoticeTService")
public class LawExamNoticeTServiceImpl extends ServiceImpl<LawExamNoticeTDao, LawExamNoticeTEntity> implements LawExamNoticeTService {

    @Autowired
    private LawExamUserTService lawExamUserTService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LawExamNoticeTEntity> page = this.page(
                new Query<LawExamNoticeTEntity>().getPage(params),
                new QueryWrapper<LawExamNoticeTEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void readNotice() {
        SysUserTokenEntity userEntity = ShiroUtils.getUserEntity();
        if(null == userEntity){
            return;
        }
        LawExamUserTEntity lawExamUserTEntity = lawExamUserTService.getOne(
                new QueryWrapper<LawExamUserTEntity>()
                .lambda()
                .eq(LawExamUserTEntity::getIdcard, userEntity.getIdCardNo())
        );
        if(null == lawExamUserTEntity){
            return;
        }
        //0是未读，1是已读
        if("0".equals(lawExamUserTEntity.getIsRead()) || null == lawExamUserTEntity.getIsRead()){
            lawExamUserTEntity.setIsRead("1");
            lawExamUserTService.updateById(lawExamUserTEntity);
        }
    }

}