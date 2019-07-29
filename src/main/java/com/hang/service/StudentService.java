package com.hang.service;


import com.hang.constant.SchoolConstant;
import com.hang.dao.StudentDAO;
import com.hang.dao.UserInfoDAO;
import com.hang.enums.ResultEnum;
import com.hang.exceptions.ApiAssert;
import com.hang.exceptions.ApiException;
import com.hang.manage.UserCache;
import com.hang.pojo.data.StudentDO;
import com.hang.pojo.data.UserInfoDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author zhanghang
 */
@Service
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseCrawlerService courseCrawlerService;

    @Autowired
    private UserCache userCache;


    /**
     * 学生绑定学号和密码
     *
     * @param openId
     * @param account
     * @apiNote 在官网做模拟登陆，进行信息的校验，同时将学生的最新课表爬取下来
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindForStudent(String openId, String account, String code) {
        Integer flag=courseCrawlerService.turnToCourse(account,code);
        if (flag==0){
            throw new ApiException(ResultEnum.JWC_ACCOUNT_OR_CODE_ERROR);
        }
        else {
            userService.updateJwcAccount(openId, account);
            SchoolConstant schoolConstant=new SchoolConstant();
            UserInfoDO userInfoDO = userInfoDAO.selectByOpenId(openId);
            StudentDO studentDO = new StudentDO();
            studentDO.setJwcAccount(userInfoDO.getJwcAccount());
            studentDO.setNickName(userInfoDO.getNickName());
            studentDO.setGrade(userInfoDO.getJwcAccount().substring(1, 5));
            studentDO.setOpenId(userInfoDO.getOpenId());
            studentDO.setDepartment(schoolConstant.getDepartment(account));
            studentDO.setAvatar(userInfoDO.getAvatarUrl());
            studentDO.setCode(code);
            StudentDO studentInfo = getStudentInfoByOpenId(openId);
            if (Objects.isNull(studentInfo)) {
                saveStudentInfo(studentDO);
            } else {
                modifyStudentInfo(studentDO);
            }
            userService.updateUserRole(openId,0);
            //redis缓存穿透
            UserInfoDO userInfo=userInfoDAO.selectByOpenId(openId);
            userCache.updateUserInfo(openId,userInfo);
        }
    }


    /**
     * 获取学生信息
     * 为了防止信息没有给全，没有输入学号
     *
     * @param openId
     * @return
     */
    public StudentDO getStudentInfoByOpenId(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new ApiException(-1, "openId为空");
        }
        StudentDO studentDO = studentDAO.selectStudentDOByOpenId(openId);
        if (studentDO != null) {
            if (StringUtils.isBlank(studentDO.getJwcAccount())) {
                studentDO.setJwcAccount("未绑定");
            }
        }
        return studentDO;
    }

    /**
     * 根据学号查
     */
    public StudentDO getStudentInfoByJwcAccount(String jwcAccount) {
        if (StringUtils.isBlank(jwcAccount)) {
            throw new ApiException(-1, "openId为空");
        }
        StudentDO studentDO = studentDAO.getStudentInfoByJwcAccount(jwcAccount);
        if (studentDO != null) {
            return studentDO;
        }
        return null;
    }



    public void saveStudentInfo(StudentDO studentDO) {
        int i = studentDAO.insert(studentDO);
        ApiAssert.nonEqualInteger(i, 1, "保存失败");
    }

    public void modifyStudentInfo(StudentDO studentDO) {
        if (StringUtils.isNotBlank(studentDO.getJwcAccount())) {
            studentDO.setGrade(studentDO.getJwcAccount().substring(1, 5));
        }
        int i = studentDAO.update(studentDO);
        ApiAssert.nonEqualInteger(i, 1, "更新失败");
    }


    public List<StudentDO> studentList(int start, int offset) {
        List<StudentDO> studentDOS = studentDAO.list(start, offset);
        return studentDOS;
    }

}
