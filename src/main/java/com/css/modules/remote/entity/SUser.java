package com.css.modules.remote.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * SUser entity. @author MyEclipse Persistence Tools
 */

public class SUser implements java.io.Serializable {

    // Fields
    private String uuid;	//uuid
    private String realName;//姓名
    private String loginName;//登录名
    private String password;//密码
    private Integer sex;	//性别
    private String mobile;	//手机号
    private String email;	//邮件
    private String userType;//用户类型
    private String delFlag;//删除标记
    private String openFlag;//开启状态
    private Date issueDate;//创建时间
    private Date editDate;//修改时间
    private String remark;//备注
    private Date lastLoginTime;//上次登录时间
    private Integer totalLoginCount;//总登录次数
    private Integer failedLoginCount;//失败登录次数
    private Date failLockTime;//上次登录时间
    private String orgId;	//组织Id
    private Date editPwdTime;//修改密码时间
    private String activeStatus;//激活状态
    private Date activeDeadLine;//激活时间
    private String provinceId;	//省份Id
    private String cityId;		//城市Id
    private String pcStatus;	//PC端制证状态
    private String appStatus;	//移动端制证状态
    private String pwdExprityTime;//密码失效分钟数
	private Integer orderNum;	//排序号
	private String creatorId;//创建人ID
	private String updateUserId;//修改人员ID
/*
    // Constructors
    // 功能列表
    private java.util.Set<String> functions = new java.util.HashSet<String>();
    // 功能点列表
    private java.util.Set<String> funcActions = new java.util.HashSet<String>();
    // 可以访问的系统
    private List<SSys> syss = new ArrayList<SSys>();
    // 所属部门
    private List<SOrgDept> depts = new ArrayList<SOrgDept>();
    // 所拥有角色
    private List<SRole> roles = new ArrayList<SRole>();
    // 所拥有岗位
    private List<SPost> posts = new ArrayList<SPost>();
    // 所关联岗位
    private List<SUserPost> userPosts = new ArrayList<SUserPost>();
    // 所关联部门
    private List<SUserDept> userDepts = new ArrayList<SUserDept>();
    //所属机构
    private List<SOrg> orgs = new ArrayList<SOrg>();
    //功能列表
    private List<SFunc> funcs = new ArrayList<SFunc>();
    //功能点列表
    private List<SFuncAction> actions = new ArrayList<SFuncAction>();

    */

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(String openFlag) {
        this.openFlag = openFlag;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getTotalLoginCount() {
        return totalLoginCount;
    }

    public void setTotalLoginCount(Integer totalLoginCount) {
        this.totalLoginCount = totalLoginCount;
    }

    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public Date getFailLockTime() {
        return failLockTime;
    }

    public void setFailLockTime(Date failLockTime) {
        this.failLockTime = failLockTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Date getEditPwdTime() {
        return editPwdTime;
    }

    public void setEditPwdTime(Date editPwdTime) {
        this.editPwdTime = editPwdTime;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Date getActiveDeadLine() {
        return activeDeadLine;
    }

    public void setActiveDeadLine(Date activeDeadLine) {
        this.activeDeadLine = activeDeadLine;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPcStatus() {
        return pcStatus;
    }

    public void setPcStatus(String pcStatus) {
        this.pcStatus = pcStatus;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getPwdExprityTime() {
        return pwdExprityTime;
    }

    public void setPwdExprityTime(String pwdExprityTime) {
        this.pwdExprityTime = pwdExprityTime;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}