package com.kefu.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author:付风松
 * @Description:
 * @Date:Created in  20:37 2018/10/29
 * @ModefiedBy:
 */
@WebFilter(urlPatterns = "/*", filterName = "crosFilter")
public class CrosFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "APIClientToken,APIClientKey,Content-Type,userKey,userToken,X-Requested-With, Content-Type, X-Auth-Token, Origin, Authorization, Date, DateTime, AuthenToken");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        response.setHeader("Access-Control-Max-Age", "1800");//30 min
        response.setHeader("Content-Type", "application/json");
        //response.setHeader("Cache-Control", "no-cache, private");
        response.setCharacterEncoding("utf-8");
        //设定编码
        response.setCharacterEncoding("UTF-8");
        //表示是json类型的数据
        response.setContentType("application/json");
        chain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {

    }
}
