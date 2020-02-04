package com.css.modules.expert.entity;

import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 专家库
 * 
 * @author liukai
 * @date 2019-11-12 17:17:24
 */
@Data
public class BumExpertAndExperienceTEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 专家姓名
	 */
    @NotBlank(message="专家姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 性别
	 */
    @NotBlank(message="性别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String gender;
	/**
	 * 专家所属地区
	 */
	private String area;
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
    @NotBlank(message="省不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String province;
    @NotBlank(message="市不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String city;
    private String status;
    /**
     * 执法经历
     */
    private List<BumExpertExperienceTEntity> bumExpertExperienceTEntitys;

}
