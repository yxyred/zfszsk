package com.css.modules.jgzfkuserinfo.entity;

import com.css.common.entity.BaseEntity;
import lombok.Data;

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
public class PubJgZfkUserInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
	private String agentname;
	/**
	 * 执法证号
	 */
	private String code;
	/**
	 * 证件有效期
	 */
	private Date validityDocuments;
    /**
     * 单位
     */
    private String unit;

}
