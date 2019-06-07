package com.hang.api;

import com.hang.pojo.data.AdminUserInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;


/**
 * @author hangs.zhang
 * @date 19-5-11
 * *****************
 * function:
 * 后台管理界面跳转控制器
 */
@Slf4j
@Controller
@RequestMapping("/page")
public class PageController {

    /**
     * 将管理员的账号和密码写死，后续在做修改
     */
    private static  final String AdminName="admin";

    private static final String AdminPwd="admin1204";

    @GetMapping("/{page}")
    public String page1(@PathVariable String page) {
        return page;
    }

    /**
     * 设置默认打开地址http://localhost:8088的跳转(需要在拦截器中排除)
     * 1.已登录，跳转到menu.html，把adminUserInfo返回前端渲染
     * 2.未登录，跳转到登录页
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String menu(HttpServletRequest request, ModelMap modelMap){
        HttpSession session = request.getSession();
        AdminUserInfoDO adminUserInfoDo = (AdminUserInfoDO) session.getAttribute("adminUserInfoDo");
        if(null != adminUserInfoDo){
            modelMap.addAttribute("adminUserInfo", adminUserInfoDo);
            return "menu";
        }else{
            return "login";
        }
    }

    /**
     * 登录(需要在拦截器中排除)
     * 1.已登录，跳转到menu.html，把adminUserInfo返回前端渲染
     * 2.未登录，验证密码，如果密码正确，跳转到menu.html, 则把用户信息放入session，并把adminUserInfo返回前端渲染
     *                     如果密码错误，跳转到login.html
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(value = "userName", required = false) String userName,
                        @RequestParam(value = "userPwd", required = false) String userPwd,
                        @RequestParam(value = "verifyCode", required = false) String verifyCode,
                        HttpServletRequest request,
                        ModelMap modelMap){

        //先验证session，再验证密码
        HttpSession session = request.getSession();
        AdminUserInfoDO adminUserInfoDo = (AdminUserInfoDO) session.getAttribute("adminUserInfoDo");
        if(null != adminUserInfoDo){
            modelMap.addAttribute("adminUserInfo", adminUserInfoDo);
            return "menu";
        }else{
            //验证密码
            System.out.println("验证登录...userName="+userName+"userPwd="+userPwd+"verifyCode="+verifyCode);
            //验证码暂时没写
            if(userPwd.equals(AdminPwd)&&userName.equals(AdminName)){
                //用户信息放入session
                AdminUserInfoDO adminUserInfoDO0 =new AdminUserInfoDO();
                System.out.println("登录...成功..." + "用户名：" + userName);
                request.getSession().setAttribute("adminUserInfo", adminUserInfoDO0);
                modelMap.addAttribute("adminUserInfo", adminUserInfoDO0);
                return "menu";
            }else{
                System.out.println("登录...密码输入错误..." + "用户名：" + userName);
                return "login";
            }
        }
    }

}

