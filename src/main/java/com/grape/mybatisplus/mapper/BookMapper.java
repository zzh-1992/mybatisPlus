/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grape.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.grape.mybatisplus.entity.Book;

/**
* @author zhihuangzhang
* @description 针对表【book(书-树状结构)】的数据库操作Mapper
* @createDate 2022-08-13 19:30:56
* @Entity com.grape.mybatisplus.entity.Book
*/
public interface BookMapper extends BaseMapper<Book> {

}
