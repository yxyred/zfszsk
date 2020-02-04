package com.css.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 远程role
 */
@Data
public class RRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uuid;
    private String name;
    private String roleType;
    private String sysId;
    private String delFlag;
    private String openFlag;
    private String remark;

}
