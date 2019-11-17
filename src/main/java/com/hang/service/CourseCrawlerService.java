package com.hang.service;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
    public Integer login(String USERNAME, String PASSWORD) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(INSIDE_COURSE_URL);
        String con = null;
        flag = 0;

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("userName", USERNAME));
        nvps.add(new BasicNameValuePair("passWord", PASSWORD));

        /*设置字符*/
        post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        /*尝试登陆*/
        HttpResponse httpResponse;

        try {
            //执行登陆
            httpResponse = httpclient.execute(post);
            HttpEntity httpEntity = httpResponse.getEntity();
            con = EntityUtils.toString(httpEntity, "utf-8");
            if (con.equals("1")) {
                flag = 1;
            }
            //关闭登陆结果集
            httpResponse.getEntity().getContent().close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 爬取课程功能调用器
     *
     * @apiNote flag代表登陆状态
     */
    public Integer turnToCourseSpider(String USERNAME, String PASSWORD) {
        return login(USERNAME, PASSWORD);
    }
}