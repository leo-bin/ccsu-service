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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author free-go
 * @Date Created in 21:42 2019/7/29
 **/
@Component
public class LatestCrawlerService extends java.util.TimerTask {

    private static final String URL = "http://www.ccsu.cn/zdxw/zdyw/232.htm";    //长大要闻URL

    @Autowired
    private ArticleDAO articleDAO;


    public Elements UrlGet() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity en = response.getEntity();
            String con = EntityUtils.toString(en, "utf-8");
            Document doc = Jsoup.parse(con);
            return doc.select("div.news_n a");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }

        }
        return null;
    }

    public void saveUrlParse(Elements elements) throws Exception {
        ArticleDO articleDO = new ArticleDO();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String arr[] = {"数学", "互联网", "程序设计"};
            if (element.text().contains(arr[0]) || element.text().contains(arr[1])
                    || element.text().contains(arr[2])) {
                String url = "http://www.ccsu.cn/" + element.attr("href").replace("../", "");
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = null;
                try {
                    response = httpclient.execute(httpGet);
                    HttpEntity en = response.getEntity();
                    String con = EntityUtils.toString(en, "utf-8");
                    Document doc = Jsoup.parse(con);
                    String title = doc.select("div>h1").text();
                    String src = String.valueOf(doc.select("div.v_news_content img").attr("src"));
                    doc.select("div.v_news_content img").attr("src", "http://www.ccsu.cn" + src);
                    String text = String.valueOf(doc.select("div.v_news_content"));
                    String time = doc.select("div.time").text();
                    time = time.replaceAll(String.valueOf((char) 160), " ");
                    String[] array = time.split("\\s+");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date release_time = formatter.parse(array[0].replaceAll("发布时间：", ""));
                    String authors = array[1].replaceAll("作者：", "");
                    articleDO.setTitle(title);
                    articleDO.setContent(text);
                    articleDO.setAuthors(authors);
                    articleDO.setRelease_time(release_time);
                    articleDO.setCategory("NOTIFICATION");
                    articleDO.setCategory_name("通知");
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
    }
    @Scheduled(cron = "0 0 2 ? * 1" )       //每周日凌晨两点启动
    public void run() {
        try {
            saveUrlParse(UrlGet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
