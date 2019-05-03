package com.hang.service;

import com.hang.dao.StudentDAO;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.StudentDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (StringUtils.isBlank(studentDO.getJwcAccount())) {
            studentDO.setJwcAccount("未绑定");
        }
        return studentDO;
    }

    public void saveStudentInfo(StudentDO studentDO) {
        int i = studentDAO.insert(studentDO);
        if (i != 1) {
            throw new ApiException(-1, "保存失败");
        }
    }

    public void modifyStudentInfo(StudentDO studentDO) {
        if (StringUtils.isNotBlank(studentDO.getJwcAccount())) {
            studentDO.setGrade(studentDO.getJwcAccount().substring(1, 5));
        }

        int i = studentDAO.update(studentDO);
        if (i != 1) {
            throw new ApiException(-1, "更新失败");
        }
    }

}
