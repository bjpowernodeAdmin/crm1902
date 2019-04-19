package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * author : 动力节点
 * 2019/4/9 0009
 */
public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户模块控制器");

        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path)){

            login(request,response);

        }else if("/settings/user/xxx.do".equals(path)){

            //xxx(request,response);

        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行验证登录操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        //取得ip地址
        String ip = request.getRemoteAddr();

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        /*
        * 我们以自定义异常的方式来处理验证登录操作
        * 调用业务层执行登录验证的业务，如果业务层出现了验证失败的问题，则为上一层（当前控制器）抛出自定义异常
        * 如果业务层为控制器抛出异常了，则证明登录失败
        * 如果业务层执行完毕后，没有为控制器抛出异常，则证明登录成功
        *
        * 为什么要使用自定义异常？
        *   因为我们在做登录验证的过程中，验证失败的种类有很多，遇到了什么样的验证失败方式，就抛出什么样的异常信息
        *
        * */

        try {

            User user = us.login(loginAct,loginPwd,ip);

            //登录成功后，将user对象存放到session域对象中
            request.getSession().setAttribute("user", user);

            //{"success":true}
            PrintJson.printJsonFlag(response, true);


        }catch (Exception e){

            //取得错误消息
            String msg = e.getMessage();

            //{"success":false,"msg":"?"}
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success", false);
            map.put("msg", msg);

            PrintJson.printJsonObj(response, map);

        }









    }
}















































