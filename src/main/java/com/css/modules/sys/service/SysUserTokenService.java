package com.css.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.css.modules.sys.entity.SysUserTokenEntity;
import com.css.common.utils.R;

/**
 * 用户Token
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(String userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(String userId);

	SysUserTokenEntity selectUserTokenByUserId(String userId);

}
