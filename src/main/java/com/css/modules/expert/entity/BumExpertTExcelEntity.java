package com.css.modules.expert.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.css.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 专家库
 * 
 * @author liukai
 * @date 2019-11-12 17:17:24
 */
@Data
public class BumExpertTExcelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Excel(name = "序号")
    private String id;
    /**
     * 专家姓名
     */
    @Excel(name = "姓名", isImportField = "true")
    private String name;
    /**
     * 性别
     */
    @Excel(name = "性别", isImportField = "true")
    private String gender;
    /**
     * 省 存储code
     */
    @Excel(name = "省", isImportField = "true")
    private String province;
    /**
     * 市 存储文字
     */
    @Excel(name = "市（区）", isImportField = "true")
    private String city;

    /**
     * 工作单位
     */
    @Excel(name = "工作单位", isImportField = "true")
    private String unitName;
    /**
     * 专家技术专长
     */
    @Excel(name = "技术专长", isImportField = "true")
    private String expertise;
    private List<String> expertises;
    /**
     * 专家身份证号
     */
    @Excel(name = "身份证号", isImportField = "true")
    private String cert;
    /**
     * 专家电话号
     */
    @Excel(name = "电话号码", isImportField = "true")
    private String tel;

    /**
     * 辅法经历
     */
    @Excel(name = "辅法经历", isImportField = "true")
    private String experience;


    /**
     * 辅法类别名称
     */
    @Excel(name = "辅法类别", isImportField = "true")
    private String experienceType;
    private List<String> experienceTypes;

}
