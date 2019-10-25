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

import static com.hang.constant.SchoolConstant.LOGIN_URL;
import static com.hang.constant.SchoolConstant.HUANCHON_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author free-go
 * @date 2019/7/19
 * @function: 网络爬虫服务层
 */
@Service
public class CourseCrawlerService {

    @Autowired
    private CourseDAO courseDAO;

    public  Integer flag = 0;

    /**
     * 登陆到教务系统
     *
     * @param USERNAME 用户名
     * @param PASSWORD 密码
     */
    public HttpClient login(String USERNAME, String PASSWORD) {
        flag=0;
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpost = new HttpPost(LOGIN_URL);
        HttpPost post=new HttpPost(HUANCHON_URL);
        String con=null;
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        nvps.add(new BasicNameValuePair("USERNAME", USERNAME));
        nvps.add(new BasicNameValuePair("PASSWORD", PASSWORD));
        nvps.add(new BasicNameValuePair("useDogCode", ""));
        nvps.add(new BasicNameValuePair("x", "37"));
        nvps.add(new BasicNameValuePair("y", "11"));

        /*设置字符*/
        httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

        /*尝试登陆*/
        HttpResponse response;

        /*尝试缓冲登陆*/
        HttpResponse httpResponse;

        try {
            //执行登陆
            response = httpclient.execute(httpost);
            //执行缓冲登陆
            httpResponse=httpclient.execute(post);
            HttpEntity httpEntity = httpResponse.getEntity();
            con = EntityUtils.toString(httpEntity, "utf-8");
            //关闭登陆结果集
            response.getEntity().getContent().close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(con);
        Elements elements=doc.getElementsByAttributeValue("name","学生专区");
        //登不上官网
        if (elements.toString().length()<1) {
            flag=2;
        }
        return httpclient;
    }


    /**
     * 爬取课程功能调用器
     *
     * @apiNote flag代表登陆状态
     */
    public Integer turnToCourseSpider(String USERNAME, String PASSWORD) {
        //如果数据库里有当前学期的课程就不需要在绑定学号的时候重复写数据了
        SchoolConstant schoolConstant = new SchoolConstant();
        String xueqi = schoolConstant.getTerm();
        Integer state=0;

        return state;
    }
}