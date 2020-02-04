package com.css.modules.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 专家辅法经历表
 * 
 * @author liukai
 * @date 2019-11-18 14:44:19
 */
@Data
@TableName("BUM_EXPERT_EXPERIENCE_T")
public class BumExpertExperienceTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 关联专家表
	 */
	private String expertId;
	/**
	 * 辅法经历
	 */
	@NotBlank(message="辅法经历不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String experience;
	/**
	 * 辅法类别
	 */
    @NotBlank(message="辅法类别不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String experienceType;
	/**
	 * 辅法类别名称
	 */
	private String experienceTypeName;


}
