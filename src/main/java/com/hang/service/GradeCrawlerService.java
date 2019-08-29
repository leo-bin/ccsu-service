package com.hang.service;

import com.hang.constant.SchoolConstant;
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
     * 登陆到教务系统
     *
     * @param USERNAME 用户名
     * @param PASSWORD 密码
     * @return 成功返回true 失败返回false
     */
    public HttpClient login(String USERNAME, String PASSWORD) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpost = new HttpPost(LOGIN_URL);
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
        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpclient;
    }

    /**
     * 获取成绩
     */
    public HttpEntity getCurriculum(HttpClient httpClient, String xueqi) {
        String SURL = "http://jwcxxcx.ccsu.cn/jwxt/xszqcjglAction.do?method=queryxscj&kksj=" + xueqi + "&kcxz=&kcmc=&xsfs=qbcj&kcdl=&kssj=&ok=";
        HttpPost httpPost = new HttpPost(SURL);
        HttpResponse re;
        try {
            re = httpClient.execute(httpPost);
            HttpEntity en = re.getEntity();
            re.getEntity().getContent().close();
            return en;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存成绩
     * @apiNote 解析成绩源码并存入数据库
     */
    public void Save(String con) {
        GradeDO gradeDO = new GradeDO();
        Document doc = Jsoup.parse(con);
        Elements elements = doc.select("tr[id]");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String[] array = element.text().split(" ");
            gradeDO.setJwcAccount(array[1]);
            gradeDO.setCourseName(array[4]);
            gradeDO.setScore(array[5]);
            gradeDO.setCategory(array[8]);
            gradeDO.setCredit(Float.valueOf(array[10]));
            gradeDO.setProperty(array[11]);
            gradeDO.setXnxq(array[3]);
            gradeDAO.addGrade(gradeDO);
        }
    }

    /**
     * 爬取成绩功能调用器
     */
    public void turnTOGrade(String userName, String code,String xueqi) {
        HttpEntity en = getCurriculum(login(userName, code), xueqi);
        try {
            String con = EntityUtils.toString(en, "utf-8");
            Save(con);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}