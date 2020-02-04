package com.css.modules.lawprojectlistt.entity;

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
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 执法事项清单
 * 
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
@Data
@TableName("LAW_PROJECT_LIST_T")
public class LawProjectListTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	@Excel(name = "序号",orderNum = "1")
	private String id;
	/**
	 * 执法主体
	 */
	private String lawMain;

	/**
	 * 执法主体
	 */
	@JsonIgnore
	@Excel(name = "执法主体",width = 35,orderNum = "2", isImportField = "true")
	@TableField(exist = false)
	private String lawMainStr;

	/**
	 * 执法事项
	 */
	@Excel(name = "执法事项",width = 30,orderNum = "3", isImportField = "true")
    @NotBlank(message="执法事项不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@NotBlank(message="执法主体不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawMatter;
	/**
	 * 执法类别
	 */
	@NotBlank(message="执法类别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String lawTypeId;
	/**
	 * 执法类别
	 * 用于统计
	 */
	@JsonIgnore
	@Excel(name = "执法类别",width = 30,orderNum = "4", isImportField = "true")
	@TableField(exist = false)
	private String lawTypeName;
	/**
	 * 执法类别描述
	 */
	@Excel(name = "执法类别描述",width = 30,orderNum = "5", isImportField = "true")
    @NotBlank(message="执法类别描述不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 1000 ,message = "执法类别描述最大长度为1000", groups = {AddGroup.class, UpdateGroup.class})
	private String lawTypeDesc;
	/**
	 * 执法依据
	 */
	@Excel(name = "执法依据",width = 30,orderNum = "6", isImportField = "true")
    @NotBlank(message="执法依据不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 1000 ,message = "执法依据最大长度为500文字", groups = {AddGroup.class, UpdateGroup.class})
	private String lawBassis;
	/**
	 * 执法程序
	 */
	@Excel(name = "执法程序",width = 30,orderNum = "7", isImportField = "true")
    @NotBlank(message="执法程序不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 1000 ,message = "执法程序最大长度为500文字", groups = {AddGroup.class, UpdateGroup.class})
	private String lawProcedure;
	/**
	 * 发布状态
	 * 0-默认，1-发布 2-撤销
	 */
	@Excel(name="发布状态",replace = {"未发布_0","已发布_1","已撤销_2"," _null"},orderNum = "8")
	@TableField(value = "ISSUE_STATUS")
	private String pubStatus;
	/**
	 * 发布日期
	 */
	@Excel(name="发布日期",orderNum = "9",format = "yyy-MM-dd")
	@JsonFormat(pattern = "yyy-MM-dd",timezone = "GMT+8")
	@TableField(value = "ISSUE_DATE")
	private Date pubDate;


}
