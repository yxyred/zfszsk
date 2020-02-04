package com.css.modules.lawsubjectt.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 市场主体名录表
 * 
 * @author
 * @email
 * @date 2019-11-18 14:26:25
 */
@Data
@TableName("LAW_SUBJECT_T")
public class LawSubjectTEntity extends BaseEntity {
	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	@Excel(name = "序号",orderNum = "1")
	private String id;
	/**
	 * 抽查检查对象统一社会信用代码
	 */
	@Excel(name = "市场主体统一社会信用代码",orderNum = "2", isImportField = "true")
    @NotBlank(message="市场主体统一社会信用代码不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String uniscid;
	/**
	 * 市场主体ID
	 */
	private String subjectId;
    /**
     * 市场主体
     */
    @Excel(name = "市场主体名称",orderNum = "3", isImportField = "true")
    @NotBlank(message="市场主体不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String subjectName;

	/**
	 * 来源(省份区划代码部门编码)
	 */
	private String sourceId;

	/**
	 * 省 存储code
	 */
	@Excel(name="省",orderNum = "4", isImportField = "true")
    @NotBlank(message="省不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String province;
	/**
	 * 市 存储文字
	 */
	@Excel(name="市（区）",orderNum = "5", isImportField = "true")
    @NotBlank(message="市（区）不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String city;

	/**
	 * 主营业务范围
	 */
	@Excel(name = "主营业务范围",orderNum = "6", isImportField = "true")
    @NotBlank(message="主营业务范围不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String buzSco;

	/**
	 * 企业成立时间
	 */
	@Excel(name = "企业成立时间",format = "yyyy-MM-dd",orderNum = "7", isImportField = "true")
	@NotNull(message="企业成立时间不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date enterpriseCreateDate;

	/**
	 * 企业成立时间 用于统计
	 * 格式化到年
	 */
	@JsonIgnore
	@TableField(exist = false)
	private String enterpriseCreateDateStr;


	/**
	 * 发布状态
	 * 0-默认，1-发布 2-撤销
	 */
	@TableField("RESOURCE_PUBSTATE")
    @JsonIgnore
	private String pubStatus;

/*
   @Excel(name = "来源",orderNum = "4")
	private String sourceName;
*/

}
