package com.hang.pojo.data;

import lombok.Data;

import java.util.Date;

/**
 * @Author free-go
 * @Date Created in 9:54 2019/7/24
 **/

@Data
public class ArticleDO {

    private String title;

    private String content;

    private String authors;

    private Date release_time;

    private String category;

    private String category_name;
}
