package com.css.modules.jgzskcase.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 执法案例库
 * 
 * @author
 * @email
 * @date 2019-11-13 15:59:02
 */
@Data
@TableName("JG_ZSK_CASE_T")
public class JgZskCaseEntity extends BaseEntity {
	/**
	 * $column.comments
	 */
	@TableId(type = IdType.UUID)
	@Excel(name = "序号",orderNum = "1")
	private String id;
	/**
	 * 案例名称
	 */
	@Excel(name = "案例名称",orderNum = "2", isImportField = "true")
	@NotBlank(message="案例名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String caseName;
	/**
	 * 案例编号
	 */
	@Excel(name = "案例编号",orderNum = "3")
	private String caseNumber;
	/**
	 * 责任单位
	 */
	@Excel(name = "责任单位",orderNum = "4", isImportField = "true")
    @NotBlank(message="责任单位不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String responsibleUnit;
	/**
	 * 结案日期
	 */
	@Excel(name = "结案日期",orderNum = "5",format = "yyyy-MM-dd", isImportField = "true")
	private Date settlementDate;
	/**
	 * 结案日期
	 * 用于统计 格式化到年
	 */
	@JsonIgnore
	@TableField(exist = false)
	private String settlementDateStr;

	/**
	 * 内容分类
	 */
//	@Excel(name = "内容分类",orderNum = "6")
//	private String contentType;

	/**
	 * 执法类别
	 */
	@NotBlank(message="内容分类不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawTypeId;
	/**
	 * 执法类别名称
	 * 用于统计
	 */
	@JsonIgnore
	@Excel(name = "内容分类",orderNum = "6", isImportField = "true")
	@TableField(exist = false)
	private String lawTypeName;
	/**
	 * 公开方式
	 */
	@Excel(name = "公开方式",orderNum = "7", isImportField = "true")
    @NotBlank(message="公开方式不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String openWay;
	/**
	 * 公开范围
	 */
	@Excel(name = "公开范围",orderNum = "8", isImportField = "true")
    @NotBlank(message="公开范围不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String openRange;
	/**
	 * 公开时限
	 */
	@Excel(name = "公开时限",orderNum = "9",format = "yyyy-MM-dd", isImportField = "true")
    @NotNull(message="公开时限不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Date openTime;
	/**
	 * 案情简介
	 */
	@Excel(name = "案情简介",orderNum = "10", isImportField = "true")
    @NotBlank(message="案情简介不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String briefIntroduction;
	/**
	 * 法律分析
	 */
	@Excel(name = "法律分析",orderNum = "11", isImportField = "true")
    @NotBlank(message="法律分析不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String legalAnalysis;
	/**
	 * 是否为典型 --“1”为典型，“0”为非典型（默认）
	 */
	private String typicalCase;
	/**
	 * 部门类别
	 */
	private String departmentType;
	/**
	 * 业务类别
	 */
	@TableField(value = "BUSINESS_CATEGORY")
	private String businessType;
	/**
	 * 监测事项
	 */
	private String monitoringMatters;
	/**
	 * 领域
	 */
	private String field;
	/**
	 * 起因
	 */
	private String cause;
	/**
	 * 过程
	 */
	private String process;
	/**
	 * 处置
	 */
	private String disposal;
	/**
	 * 结果
	 */
	@TableField(value = "CASE_RESULT")
	private String result;
	/**
	 * 结论
	 */
	private String conclusion;
	/**
	 * 发布日期
	 */
	private Date releaseDate;

}
