package com.css.modules.sys.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.css.common.utils.SsoUrlUtils;
import com.css.modules.remote.util.HttpRequestUtils;
import com.css.modules.sys.dao.SysMenuDao;
import com.css.modules.sys.entity.SysMenuEntity;
import com.css.modules.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

	@Autowired
	private HttpRequestUtils httpRequestUtils;
	@Autowired
	private SsoUrlUtils ssoUrlUtils;
	
	/*@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}

		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}

		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.removeById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
	}*/

    /**
     * 获取菜单
     * 组件有个问题，使用requestBody入参有问题
     * @param userId
     * @param sysId
     * @return
     */
	@Override
	public String getMenuByUserIdAndSysId(String userId, String sysId) {
        /*MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.put("userId", Collections.singletonList(userId));
        requestBody.put("sysId", Collections.singletonList(sysId));*/
        String url = ssoUrlUtils.getGetMenu() + "?userId=" + userId + "&sysId=" + sysId;
        return httpRequestUtils.sendHttpRequestForm(url, null, HttpMethod.GET);
	}

	/**
	 * 获取所有菜单列表
	 */
	/*private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	*//**
	 * 递归
	 *//*
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		
		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}*/
}
