/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grape.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.grape.mybatisplus.entity.Book;
import com.grape.mybatisplus.mapper.BookMapper;
import com.grape.mybatisplus.vo.BookVo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@MapperScan("com.grape.mybatisplus.mapper")
@SpringBootApplication
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

    @Autowired
    BookMapper bookMapper;

    /**
     * 查询有效数据
     */
    @Bean
    CommandLineRunner initWebClientRunner() {
        int status = 1;
        return runArgs -> {
            QueryWrapper<Book> wrapper = new QueryWrapper<>();
            wrapper.isNull("parent_id");
            wrapper.eq("status", 1);
            List<Book> books = bookMapper.selectList(wrapper);

            // 方式一
            List<BookVo> voList = books.stream().map(book -> {
                BookVo vo = new BookVo();
                BeanUtils.copyProperties(book, vo);

                List<BookVo> child = queryChild(vo.getId(), status);

                vo.setChildren(child);
                return vo;
            }).collect(Collectors.toList());

            // 方式二

            System.out.println();
        };
    }

    List<BookVo> queryChild(int id, Integer status) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(status)) {
            queryWrapper.eq(Book::getStatus, status);
        }

        queryWrapper.eq(Book::getParentId, id);
        return bookMapper.selectList(queryWrapper).stream().map(book -> {
            BookVo vo = entityToVo(book);

            List<BookVo> child = queryChild(vo.getId(), status);
            vo.setChildren(child);
            return vo;
        }).collect(Collectors.toList());
    }

    BookVo entityToVo(Book book) {
        BookVo vo = new BookVo();
        BeanUtils.copyProperties(book, vo);
        return vo;
    }


    /**
     * 查询无效数据
     */
    @Bean
    CommandLineRunner initWebClientRunner2() {
        return runArgs -> {
            LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Book::getStatus, -1);

            // 先查所有无效数据
            List<BookVo> books = bookMapper.selectList(wrapper).stream().map(this::entityToVo).collect(Collectors.toList());

            List<BookVo> allList = new ArrayList<>(books);

            // 获取已经查询的无效数据的id
            List<Integer> idList = books.stream().map(BookVo::getId).collect(Collectors.toList());

            // 获取第一次的所有父级id
            List<Integer> parentIdList = books.stream().map(BookVo::getParentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            if (!parentIdList.isEmpty()) {
                // 除去已经查询的父级id;
                parentIdList.removeAll(idList);
                queryByIdList(parentIdList, allList);
            }

            // 将list转换为tree

            // 先获取顶层数据
            List<BookVo> collect = allList.stream().filter(bookVo -> bookVo.getParentId() == null).collect(Collectors.toList());
            collect.forEach(book -> {
                // 获取顶层数据的id，也就是子数据的parent_id
                handleChild(book, allList);
            });
            System.out.println();
        };
    }

    private void handleChild(BookVo book, List<BookVo> allList) {
        List<BookVo> bookVos = allList.stream()
                // 子元素的parentId 和父级数据的id相同
                .filter(bookVo -> Objects.equals(bookVo.getParentId(), book.getId()))
                // 收集
                .collect(Collectors.toList());
        bookVos.forEach(childBook -> handleChild(childBook, allList));
        book.setChildren(bookVos);
    }

    void queryByIdList(List<Integer> idList, List<BookVo> allList) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Book::getId, idList);
        //
        List<BookVo> books = bookMapper.selectList(wrapper).stream().map(this::entityToVo).collect(Collectors.toList());
        allList.addAll(books);

        // 已经查询的id
        List<Integer> integerList = allList.stream().map(BookVo::getId).collect(Collectors.toList());

        // 获取第一次的所有父级id
        List<Integer> parentIdList = books.stream().map(BookVo::getParentId).filter(Objects::nonNull).collect(Collectors.toList());

        if (!parentIdList.isEmpty()) {
            // 除去已经查询的父级id;
            parentIdList.removeAll(integerList);
            queryByIdList(parentIdList, allList);
        }
    }
}
