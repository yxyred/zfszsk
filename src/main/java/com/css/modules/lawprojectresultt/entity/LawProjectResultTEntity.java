package com.css.modules.lawprojectresultt.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 执法结果信息
 * 
 * @author 
 * @email 
 * @date 2019-11-15 15:16:25
 */
@Data
@TableName("LAW_PROJECT_RESULT_T")
public class LawProjectResultTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	@Excel(name = "序号",orderNum = "1")
	private String id;
	/**
	 * 发布单位
	 */
	@Excel(name = "发布单位",width = 20,orderNum = "2", isImportField = "true")
    @TableField(exist = false)
	private String deptName;
	/**
	 * 发布单位ID
	 */
	@NotBlank(message="发布单位不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String deptId;
	/**
	 * 发布人员
	 */
	@Excel(name = "发布人员",orderNum = "3", isImportField = "true")
    @NotBlank(message="发布人员不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String drafter;
	/**
	 * 起草人ID
	 */
	private String drafterId;
	/**
	 * 发布人员联系电话
	 */
	@Excel(name = "发布人员联系电话",width = 18,orderNum = "4", isImportField = "true")
    @NotBlank(message="发布人员联系电话不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@TableField(value = "CONTACT_TEL")
	private String tel;
	/**
	 * 省 存储code
	 */
	@Excel(name="省",orderNum = "5", isImportField = "true")
    @NotBlank(message="省不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String province;
	/**
	 * 市 存储文字
	 */
	@Excel(name="市（区）",orderNum = "6", isImportField = "true")
    @NotBlank(message="市（区）不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String city;
	/**
	 * 执法主体
	 */
	@NotBlank(message="执法主体不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawMain;
	/**
	 * 执法主体
	 */
	@JsonIgnore
	@Excel(name = "执法主体",width = 35,orderNum = "7", isImportField = "true")
	@TableField(exist = false)
	private String lawMainStr;


	/**
	 * 执法对象
	 */
	@Excel(name = "执法对象",orderNum = "8", isImportField = "true")
    @NotBlank(message="执法对象不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawObject;
	/**
	 * 执法类别
	 */
	@NotBlank(message="执法类别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawTypeId;
	/**
	 * 执法类别名称
	 * 用于统计
	 */
	@JsonIgnore
	@Excel(name = "执法类别",orderNum = "9", isImportField = "true")
    @TableField(exist = false)
	private String lawTypeName;
	/**
	 * 执法结论
	 */
	@Excel(name = "执法结论",orderNum = "10", isImportField = "true")
    @NotBlank(message="执法结论不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawResult;
	/**
	 * 执法结果日期
	 */
	@Excel(name = "执法结果日期",width = 15,orderNum = "11",format = "yyyy-MM-dd", isImportField = "true")
    @NotNull(message="执法结果日期不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@JsonFormat(pattern = "yyy-MM-dd",timezone = "GMT+8")
	private Date lawResultTime;
	/**
	 * 执法结果日期
	 * 用于统计
	 * 将数据 格式化到年
	 */
	@JsonIgnore
	@TableField(exist = false)
	private String lawResultTimeStr;

	/**
	 * 详情（链接）
	 */
	@Excel(name = "详情（链接）",width = 45,orderNum = "12", isImportField = "true")
    @NotBlank(message="详情（链接）不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String operationUrl;
	/**
	 * 发布状态
	 * 0-默认，1-发布 2-撤销
	 */
	@Excel(name="发布状态",replace = {"未发布_0","已发布_1","已撤销_2"," _null"},orderNum = "13")
	@TableField(value = "ISSUE_STATUS")
	private String pubStatus;
	/**
	 * 发布日期
	 */
	@Excel(name="发布日期",orderNum = "14",format = "yyy-MM-dd")
	@JsonFormat(pattern = "yyy-MM-dd",timezone = "GMT+8")
	@TableField(value = "ISSUE_DATE")
	private Date pubDate;



}
