package com.hang.service;

import com.hang.constant.SchoolConstant;
import com.hang.dao.CourseDAO;
import com.hang.pojo.data.CourseDO;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.hang.constant.SchoolConstant.*;

/**
 * @author free-go
 * @date 2019/7/19
 * @function: 网络爬虫服务层
 */
@Service
public class CourseCrawlerService {

    public Integer flag = 0;

    /**
     * 登陆到内网爬虫服务器
     *
     * @param USERNAME 用户名
     * @param PASSWORD 密码
     */
    public HttpClient login(String USERNAME, String PASSWORD) {
        flag = 0;
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(INSIDE_URL);
        String con = null;

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("USERNAME", USERNAME));
        nvps.add(new BasicNameValuePair("PASSWORD", PASSWORD));


        /*设置字符*/
        post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        /*尝试登陆*/
        HttpResponse response;

        try {
            //执行登陆
            response = httpclient.execute(post);
            HttpEntity httpEntity = response.getEntity();
            con = EntityUtils.toString(httpEntity, "utf-8");
            //关闭登陆结果集
            response.getEntity().getContent().close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpclient;
    }


    /**
     * 爬取课程功能调用器
     *
     * @apiNote flag代表登陆状态
     */
    public Integer turnToCourseSpider(String USERNAME, String PASSWORD) {
        return 1;
    }
}