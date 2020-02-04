package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.css.common.entity.BaseEntity;
import com.css.common.validator.group.AddGroup;
import com.css.common.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 试卷信息表
 * 
 * @author 
 * @email 
 * @date 2019-11-26 16:39:24
 */
@Data
@TableName("LAW_EXAM_TEST_T")
public class LawExamTestTEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键\n主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 试卷名称
	 */
	@NotBlank(message="试卷名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 200 ,message = "试卷名称最大长度为200", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 分类
	 */
	@NotBlank(message="分类不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 50 ,message = "分类最大长度为50", groups = {AddGroup.class, UpdateGroup.class})
	private String category;
	/**
	 * 总数
	 */
	@NotBlank(message="总数不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 10 ,message = "总数最大长度为10", groups = {AddGroup.class, UpdateGroup.class})
	private Integer sumScore;
	/**
	 * 限制时长（分）
	 */
	@NotBlank(message="限制时长（分）不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Length(max = 6 ,message = "限制时长（分）最大长度为6", groups = {AddGroup.class, UpdateGroup.class})
	private int limitTime;
	/**
	 * 服务调用备注
	 */
	@Length(max = 150 ,message = "服务调用备注最大长度为150", groups = {AddGroup.class, UpdateGroup.class})
	private String remarks;
	/**
	 * 状态
	 * 默认 未使用 0 已使用 1
	 */
	private String status;
	/**
	 * 试卷生成时间
	 */
	private Date testCreateTime;
	/**
	 * 试卷与题相关信息
	 */
	@TableField(exist = false)
	private List<Map<String,Object>> question;

}
