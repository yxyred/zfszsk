package com.css.modules.lawsubjectt.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RemoteSubjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer accountType;    //账号类型
    private String certificatekey;   //法人机构代码散列值
    private String certificatesno;   //法人机构代码
    private Integer corpPersonSex;   //法人代表性别
    private String corpname;         //法人名称
    private String corptype;         //法人类型
    private String countryAndRegion; //所在国家和地区
    private String countyCode;       //所在县代码
    private String createdate;       //企业成立时间
    private Integer economyType;     //经济类型
    private String enterpriseAddress; //联系地址
    private String enterpriseCorpnationCode;  //企业法人代表民族
    private String enterpriseName;     //企业名称
    private String enterpriseNameen;    //企业英文名称
    private Integer enterpriseNature;    //企业性质
    private Integer enterpriseScale;     //企业规模
    private Integer enterpriseType;      //企业类型
    private String enterpriseUrl;       //企业网址
    private String enterpriseUuid;       //企业UUID
    private String enterpriseaddress;    //企业注册地址
    private String enterpriserange;      //经营范围
    private List<String> enterprisesUsed;     //企业曾用名
    private Integer gbPushFlag;      //国办推送成功标志
    private Integer isListed;       //是否上市
    private String legalCertBeginDate;    //法定代表人证件有效期起始日期
    private String legalCertEndDate;      //法定代表人证件有效期起始日期
    private String legalCertKey;         //法定代表人证件号码散列值
    private String legalMobile;        //法定代表人电话

    private String legalStatus;       //法人状态
    private String legalcertno;       //法定代表人证件号码
    private Integer legalcerttype;     //法定代表人证件类型
    private String legalname;       //法定代表人姓名
    private Integer listType;       //上市类型
    private String loginname;      //登录名
    private String mail;          //邮箱
    private Integer manufacturingCityCode;    //所在市代码
    private String manufacturingProvinceCode;    //所在省代码
    private String positions;       //导入机构名称
    private String regCapital;      //注册资本
    private Integer regaddressZipcode;    //注册地址邮编
    private Integer registerType;     //注册类型
    private String idEndTime;   //证件终止日期
    private String idStartTime;   //证件起始日期
    private String shareRatio;   //参股比例
    private String shareholderCertNum;  //股东证件号码
    private String shareholderCountry;  //股东国别
    private String shareholderName;   //股东名称
    private String shareholderType;    //股东类型
    private String shareholderUuid;    //股东UUID
    private String usedName;            //企业曾用名
    private String currentName;         //企业现用名
    private String createTime;          //更改企业名称的时间

}
