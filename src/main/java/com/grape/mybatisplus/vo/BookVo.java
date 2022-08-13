/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grape.mybatisplus.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 书-树状结构
 *
 * @TableName book
 */
@Data
public class BookVo implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 全称
     */
    private String fullName;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 状态:1 正常;-1 失效
     */
    private Integer status;

    /**
     * 子集
     */
    private List<BookVo> children;

    private static final long serialVersionUID = 1L;
}