package com.css.modules.lawprojectlistt.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.css.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 执法事项清单
 * 
 * @author 
 * @email 
 * @date 2019-11-14 14:02:12
 */
@Data
public class PubLawProjectListTEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 执法主体
	 */
	private String lawMain;
	/**
	 * 执法事项
	 */
	private String lawMatter;
	/**
	 * 执法类别
	 */
	private String lawTypeId;
	/**
	 * 执法类别
	 */
	private String lawTypeName;

	/**
	 * 执法依据
	 */
	private String lawBassis;
	/**
	 * 执法程序
	 */
	private String lawProcedure;



}
