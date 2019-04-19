package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author : 动力节点
 * 2019/4/11 0011
 */
public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("验证是否已经登录的过滤器");

        /*

            取得session对象
            从session对象中取得user对象
            判断user对象
                如果user对象为null，说明没登录过，需要重定向到登录页
                如果user对象不为null，说明登录过，直接将请求放行即可


         */

        /*

            将HttpServletRequest 和 HttpServletResponse 对象取得

         */

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        /*

            如果请求是/login.jsp，/settings/user/login.do，我们应该将请求自动放行

         */

        String path = request.getServletPath();
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){

            chain.doFilter(req, resp);

        //登录操作之外的其他请求，必须要验证有没有登录过
        }else{

            User user = (User)request.getSession().getAttribute("user");

            //如果登录过
            if(user!=null){

                chain.doFilter(req, resp);


                //如果没登录过
            }else{

            /*

                关于响应路径的写法：
                    不论转发还是重定向，肯定都是使用绝对路径（实际项目开发中，不存在相对路径）

                /项目名/具体的资源路径

                转发：
                    转发使用的是一种特殊的绝对路径的方式
                    前面不加/项目名 的
                    直接写/具体的资源路径就可以了
                    这种路径的表现形式也叫做内部路径形式

                重定向：
                    重定向使用的是传统的绝对路径的方式
                    前面必须加/项目名
             */


                //request.getRequestDispatcher("/login.jsp").forward(request, response);
                //response.sendRedirect("/crm/login.jsp");

                //request.getContextPath()： /当前项目的项目名
                //重定向到登录页
                response.sendRedirect(request.getContextPath() + "/login.jsp");

            }


        }








    }
}
