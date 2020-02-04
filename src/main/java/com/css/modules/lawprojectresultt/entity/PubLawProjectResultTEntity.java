package com.css.modules.lawprojectresultt.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 执法结果信息
 * 
 * @author 
 * @email 
 * @date 2019-11-15 15:16:25
 */
@Data
public class PubLawProjectResultTEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 发布单位
	 */
	private String deptName;
	/**
	 * 发布人员
	 */
	private String drafter;
	/**
	 * 执法主体
	 */
	private String lawMain;
	/**
	 * 执法对象
	 */
	private String lawObject;
	/**
	 * 执法类别
	 */
	private String lawTypeId;
	/**
	 * 执法类别名称
	 */
	private String lawTypeName;
	/**
	 * 执法结论
	 */
	private String lawResult;
	/**
	 * 执法结果日期
	 */
	private Date lawResultTime;
	/**
	 * 详情（链接）
	 */
	private String operationUrl;

}
