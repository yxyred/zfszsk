package com.css.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建人ID
     */
    @TableField(value = "CREATOR_ID", fill = FieldFill.INSERT) // 新增执行
    @JsonIgnore
    private String creatorId;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonIgnore
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人员ID
     */
    @TableField(value = "UPDATE_USER_ID", fill = FieldFill.INSERT_UPDATE) // 新增和更新执行
    @JsonIgnore
    private String updateUserId;

    /**
     * 更新时间
     */
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonIgnore
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date  updateTime;

    /**
     * 删除标志
     */
    @TableField(value = "DEL_FLAG")
    @TableLogic
    @JsonIgnore
    private String delFlag;



}
