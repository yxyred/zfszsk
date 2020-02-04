package com.css.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 专家库
 * 
 * @author liukai
 * @date 2019-11-12 17:17:24
 */
@Data
@TableName("BUM_EXPERT_T")
public class BumExpertTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 专家姓名
	 */
	@NotBlank(message="专家姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 性别
	 */
	@TableField("SEX")
    @NotBlank(message="性别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String gender;

	/**
	 * 省 存储code
	 */
	private String province;
	/**
	 * 市 存储文字
	 */
	private String city;
	/**
	 * 专家职务
	 */
	private String post;
	/**
	 * 专家工作单位类别
	 */
    @NotBlank(message="专家工作单位类别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String unitType;
	/**
	 * 专家工作单位类别名称
	 */
	private String unitTypeName;
	/**
	 * 工作单位
	 */
	@TableField("WORK_UNIT")
    @NotBlank(message="专家工作单位不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String unitName;
	/**
	 * 专家技术专长
	 */
	private String expertise;
	/**
	 * 专家身份证号
	 */
    @NotBlank(message="专家身份证号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String cert;
	/**
	 * 专家电话号
	 */
    @NotBlank(message="专家电话不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String tel;

    /**
     * 默认为0---未抽取，1为已抽取
     */
    private String status;

}
