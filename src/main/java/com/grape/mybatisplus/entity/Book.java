package com.grape.mybatisplus.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 书-树状结构
 *
 * @TableName book
 */
@Data
public class Book implements Serializable {
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

    private static final long serialVersionUID = 1L;
}