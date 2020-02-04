

package com.css.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.css.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统用户Token
 */
@Data
@TableName("SYS_USER_TOKEN")
public class SysUserTokenEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	@TableId(type = IdType.INPUT)
	private String userId;
	//token
	private String token;
	//过期时间
	private Date expireTime;
    //身份证
	private String idCardNo;
}
