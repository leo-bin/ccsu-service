package com.hang.dao;

import com.hang.pojo.data.ArticleDO;
import org.springframework.stereotype.Repository;

/**
 * @Author free-go
 * @Date Created in 9:57 2019/7/24
 **/

@Repository
public interface ArticleDAO {
    int addArticle(ArticleDO article);
}
