package com.hang.service;

import com.hang.dao.GradeDAO;
import com.hang.pojo.data.GradeDO;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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

import static com.hang.constant.SchoolConstant.INSIDE_GRADE_URL;
import static com.hang.constant.SchoolConstant.LOGIN_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author free-go
 * @date 2019/7/19
 * @function: 成绩爬取服务层
 */
@Service
public class GradeCrawlerService {

    @Autowired
    private GradeDAO gradeDAO;

    /**
     * 登陆到内网爬虫服务器
     *
     * @param USERNAME 用户名
     * @param PASSWORD 密码
     */
    public void login(String USERNAME, String PASSWORD) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpost = new HttpPost(INSIDE_GRADE_URL);
        String con = null;
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("userName", USERNAME));
        nvps.add(new BasicNameValuePair("passWord", PASSWORD));

        /*设置字符*/
        httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        /*尝试登陆*/
        HttpResponse response;

        try {
            //执行登陆
            response = httpclient.execute(httpost);
            HttpEntity httpEntity = response.getEntity();
            con = EntityUtils.toString(httpEntity, "utf-8");


            //关闭登陆结果集
            response.getEntity().getContent().close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 爬取成绩功能调用器
     */
    public void turnTOGrade(String userName, String code, String xueqi) {

    }
}