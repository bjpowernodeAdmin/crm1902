package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * author : 动力节点
 * 2019/4/11 0011
 */
public class EncodingFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //请求：post请求中文参数的处理
        req.setCharacterEncoding("UTF-8");

        //响应：响应流响应中文的问题
        resp.setContentType("text/html;charset=utf-8");

        //处理完字符编码后，需要将请求放行
        chain.doFilter(req, resp);

    }
}







































