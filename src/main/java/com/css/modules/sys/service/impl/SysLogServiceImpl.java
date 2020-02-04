

package com.css.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.modules.sys.dao.SysLogDao;
import com.css.modules.sys.entity.SysLogTEntity;
import com.css.modules.sys.service.SysLogService;
import com.css.common.utils.PageUtils;
import com.css.common.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogTEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");

        IPage<SysLogTEntity> page = this.page(
            new Query<SysLogTEntity>().getPage(params),
            new QueryWrapper<SysLogTEntity>().like(StringUtils.isNotBlank(key),"username", key)
        );

        return new PageUtils(page);
    }
}
