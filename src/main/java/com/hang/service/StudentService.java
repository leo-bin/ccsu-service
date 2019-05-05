package com.hang.service;

import com.hang.dao.StudentDAO;
import com.hang.exceptions.ApiAssert;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.StudentDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhanghang
 */
@Service
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    public StudentDO getStudentInfo(String openId) {
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

    public void addComprehensiveFraction(String openId, Double comprehensiveFraction) {
        StudentDO studentInfo = getStudentInfo(openId);
        if (studentInfo.getComprehensiveFraction() != null) {
            comprehensiveFraction += studentInfo.getComprehensiveFraction();
        }
        int i = studentDAO.updateComprehensiveFraction(comprehensiveFraction, openId);
        ApiAssert.nonEqualInteger(i, 1, "修改失败");
    }

    public List<StudentDO> studentList(int start, int offset) {
        List<StudentDO> studentDOS = studentDAO.list(start, offset);
        return studentDOS;
    }

}
