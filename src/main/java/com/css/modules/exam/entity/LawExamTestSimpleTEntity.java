package com.css.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * 试卷信息表 用于返回前段
 *
 * @author
 * @email
 * @date 2019-11-26 16:39:24
 */
@Data
@TableName("LAW_EXAM_TEST_T")
public class LawExamTestSimpleTEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键\n主键
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 试卷名称
     */
    private String name;


}
