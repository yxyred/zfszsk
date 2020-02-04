package com.css.modules.jgzfkuserinfo.entity;

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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 执法检查人员库
 * 
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
@Data
@TableName("JG_ZFK_USER_INFO_T")
public class JgZfkUserInfoEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 记录唯一标识
	 */
	@TableId(value = "ROWGUID",type = IdType.UUID)
	@Excel(name = "序号",orderNum = "1")
	private String recordUniqueIdentity;
	@TableField(exist = false)
	private String id;
	/**
	 * 姓名
	 */
	@Excel(name = "姓名",orderNum = "2", isImportField = "true")
	@NotBlank(message="姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 64 ,message = "姓名最大长度为64", groups = {AddGroup.class, UpdateGroup.class})
	private String agentname;
	/**
	 * 所属主体（受委托组织）全称
	 */
	private String fullNameOfTheSubject;
	/**
	 * 单位
	 */
	@Excel(name = "单位",orderNum = "3", isImportField = "true")
    @NotBlank(message="单位不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 200 ,message = "单位最大长度为200", groups = {AddGroup.class, UpdateGroup.class})
	private String unit;
	/**
	 * 身份证号
	 */
	@Excel(name = "身份证号",width = 20,orderNum = "4", isImportField = "true")
    @NotBlank(message="身份证号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 18 ,message = "身份证号最大长度为18", groups = {AddGroup.class, UpdateGroup.class})
	private String idNumber;
	/**
	 * 性别
	 */
	@Excel(name = "性别",replace = {"男_1","女_2"," _null"},orderNum = "5", isImportField = "true")
    @NotBlank(message="性别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String sex;
	/**
	 * 出生日期
	 */
	@Excel(name = "出生日期",orderNum = "6",format = "yyy-MM-dd", isImportField = "true")
	@JsonFormat(pattern = "yyy-MM-dd",timezone = "GMT+8")
    @NotNull(message="出生日期不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Date birthDate;
	/**
	 * 年龄段
	 */
	@TableField(exist = false)
	@JsonIgnore
	private String ageGroup;

	/**
	 * 政治面貌
	 */
	@Excel(name = "政治面貌",orderNum = "7")
	@TableField(value = "ZZMM")
	private String politicalStatus;
	/**
	 * 职级
	 */
	@Excel(name = "职级",orderNum = "8")
	@Length(max = 50 ,message = "职级最大长度为50", groups = {AddGroup.class, UpdateGroup.class})
	private String rank;
	/**
	 * 民族
	 */
	@Excel(name = "民族",orderNum = "9")
	@TableField(value = "NATION_CODE")
	private String nation;
	/**
	 * 最高学历
	 */
	@Excel(name = "最高学历",width = 15,orderNum = "10")
	private String highestEducation;
	/**
	 * 学历专业
	 */
	@Excel(name = "学历专业",orderNum = "11")
	@Length(max = 100 ,message = "学历专业最大长度为100", groups = {AddGroup.class, UpdateGroup.class})
	private String academicQualifications;
	/**
	 * 是否监督人员
	 */
	@Excel(name = "是否监督人员",replace = {"是_1","否_2"," _null"},orderNum = "12", isImportField = "true")
    @NotBlank(message="是否监督人员不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String supervisionStaff;
	/**
	 * 是否具有法律职业资格
	 */
	@Excel(name = "是否具有法律职业资格",replace = {"是_1","否_2"," _null"},orderNum = "13")
	private String qualificationLegalPro;
	/**
	 * 联系电话
	 */
	@Excel(name = "联系电话",width = 15,orderNum = "14", isImportField = "true")
    @NotBlank(message="联系电话不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 11 ,message = "联系电话最大长度为11", groups = {AddGroup.class, UpdateGroup.class})
	private String contactTel;
	/**
	 * 执法人员性质
	 */
	@Excel(name = "执法人员性质",width = 25,orderNum = "15", isImportField = "true")
    @NotBlank(message="执法人员性质不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawStaffType;
	/**
	 * 执法证号
	 */
	@Excel(name = "执法证号",orderNum = "16", isImportField = "true")
    @NotBlank(message="执法证号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 100 ,message = "执法证号最大长度为100", groups = {AddGroup.class, UpdateGroup.class})
	private String code;

	/**
	 * 发证机关
	 */
	@Excel(name = "发证机关",orderNum = "17")
	@Length(max = 100 ,message = "发证机关最大长度为100", groups = {AddGroup.class, UpdateGroup.class})
	private String licenceIssuingAuthority;
	/**
	 * 执法区域
	 */
	@Excel(name = "执法区域",orderNum = "18")
	@Length(max = 100 ,message = "执法区域最大长度为100", groups = {AddGroup.class, UpdateGroup.class})
	private String enforcementArea;
	/**
	 * 执法类别
	 */
	@Excel(name = "执法类别",orderNum = "19")
	private String enforcementType;
	/**
	 * 证件有效期
	 */
	@Excel(name = "证件有效期",orderNum = "20",format = "yyy-MM-dd", isImportField = "true")
    @NotNull(message="证件有效期不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@JsonFormat(pattern = "yyy-MM-dd",timezone = "GMT+8")
	private Date validityDocuments;
	/**
	 * 省 存储code
	 */
	@Excel(name="省",orderNum = "21", isImportField = "true")
    @NotBlank(message="省不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String province;
	/**
	 * 市 存储文字
	 */
	@Excel(name="市（区）",orderNum = "22", isImportField = "true")
    @NotBlank(message="市（区）不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String city;
	/**
	 * 证件是否有效
	 */
	@TableField(exist = false)
	@JsonIgnore
	private String isEffective;
	/**
	 * 发布状态
	 * 0-默认，1-发布 2-撤销
	 */
	@Excel(name="发布状态",replace = {"未发布_0","已发布_1","已撤销_2"," _null"},orderNum = "23")
	@TableField(value = "ISSUE_STATUS")
	private String pubStatus;
	/**
	 * 发布日期
	 */
	@Excel(name="发布日期",orderNum = "24",format = "yyy-MM-dd")
	@JsonFormat(pattern = "yyy-MM-dd",timezone = "GMT+8")
	@TableField(value = "ISSUE_DATE")
	private Date pubDate;

	/**
	 * 抽取状态
	 */
	private String status;


}
