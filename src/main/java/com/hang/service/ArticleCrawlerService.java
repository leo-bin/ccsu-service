package com.hang.service;

import com.hang.dao.ArticleDAO;
import com.hang.pojo.data.ArticleDO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author free-go
 * @Date Created in 9:26 2019/7/24
 * @function:文章爬虫服务层
 **/
@Component
public class ArticleCrawlerService {

    /**
     * CSDN程序人生URL
     */
    private static final String URL = "https://www.csdn.net/nav/career";

    @Autowired
    private ArticleDAO articleDAO;

    public Elements UrlGet()throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity en = response.getEntity();
            String con = EntityUtils.toString(en, "utf-8");
            Document doc = Jsoup.parse(con);
            return doc.select("h2>a[href]");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }

        }
        return null;
    }

    /**
     * 解析URL，获取源码并对其做相应处理，以便于显示于富文本
     * 分别存储标题和正文
     * @param elements
     * @throws Exception
     */
    public  void saveUrlParse(Elements elements) throws Exception{
        ArticleDO articleDO = new ArticleDO();
        for (int i = 0; i <elements.size() ; i++) {
            Element element = elements.get(i);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(element.attr("href"));
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpGet);
                HttpEntity en = response.getEntity();
                String con = EntityUtils.toString(en, "utf-8");
                Document doc = Jsoup.parse(con);
                String title = String.valueOf(doc.select("h1.title-article").text());
                String text = String.valueOf(doc.select("div#content_views")).replaceAll("\n"," ")
                        .replaceAll("'","\"").replaceAll("<br>","\n");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date time = formatter.parse(doc.select("span.time").text().replaceAll("年","-")
                        .replaceAll("月","-").replaceAll("日", ""));
                System.out.println(time);
                articleDO.setTitle(title);
                articleDO.setContent(text);
                articleDO.setRelease_time(time);
                articleDO.setAuthors("CSDN");
                articleDO.setCategory("TECHNOLOGY");
                articleDO.setCategory_name("技术");
                articleDAO.addArticle(articleDO);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }
    }

    @Scheduled(cron = "0 0 2 ? * 1")        ////每周日凌晨两点启动
    public  void run() {
        try {
            saveUrlParse(UrlGet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
