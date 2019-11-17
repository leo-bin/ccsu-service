package com.hang.service;

import com.hang.dao.InformationDAO;
import com.hang.pojo.data.InformationDO;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author free-go
 * @Date Created in 9:26 2019/7/24
 * @function: 文章爬虫服务层
 **/
@Service
public class BlogCrawlerService {
    private static final String URL = "https://www.cnblogs.com/cate/codelife/";  //博客园URL

    @Autowired
    private InformationDAO informationDAO;

    @Autowired
    private HotAndCacheService cacheService;

    public Elements doGet() {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(URL);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String con = EntityUtils.toString(responseEntity, "utf-8");
                Document doc = Jsoup.parse(con);
                return doc.select("h3>a[href]");  //返回第一页所有博客的URL

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void urlParse(Elements elements) throws Exception {
        InformationDO informationDO = new InformationDO();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(element.attr("href"));
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpGet);
                HttpEntity en = response.getEntity();
                String con = EntityUtils.toString(en, "utf-8");
                Document doc = Jsoup.parse(con);
                String title = String.valueOf(doc.select("a.postTitle2").text());
                String text = String.valueOf(doc.select("div#cnblogs_post_body")).replaceAll("\n", " ")
                        .replaceAll("'", "\"").replaceAll("<br>", "\n");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date time = formatter.parse(doc.select("span#post-date").text().replaceAll("年", "-")
                        .replaceAll("月", "-").replaceAll("日", ""));
                informationDO.setTitle(title);
                informationDO.setContent(text);
                informationDO.setReleaseTime(time);
                informationDO.setAuthors("博客园");
                informationDO.setCategory("TECHNOLOGY");
                informationDO.setCategoryName("技术");
                informationDAO.insert(informationDO);
                //写入缓存
                cacheService.addInformation2Cache(informationDO);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }


    }

    @Scheduled(cron = "0 0 2 ? * SUN")       //每周日凌晨两点启动
    public void run() {
        try {
            urlParse(doGet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}