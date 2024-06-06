package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName = "/loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter{

    //匹配器
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、 获取本次请求的JRI
        String requestURI = request.getRequestURI();

        //不需要处理的路径
        String[] uri = new String[]{
                "/employee/login",
                "/employee/logut",
                "/backend/**",
                "/front/**",
                "/common/**"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(uri, requestURI);
        //3、如果不需要处理，则直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){

            //获取empId存入threadLocal中，给自动填充是获取empId
            Long empId = (Long) request.getSession().getAttribute("employee");
            //存入threadLocal线程的局部变量中，给后面metaObject自动填充时获取empId
            BaseContext.set(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //5、如果未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查路径是否需要放行
     * @param uris
     * @param requestURI
     * @return
     */
    public boolean check(String[] uris,String requestURI){
        for (String uri : uris) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

}
