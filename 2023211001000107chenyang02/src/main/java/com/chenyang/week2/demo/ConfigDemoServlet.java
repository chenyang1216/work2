package com.chenyang.week2.demo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ConfigDemoServlet", urlPatterns = {"/config"},
        initParams = {
                @WebInitParam(name = "name1", value = "chenyang"),
                @WebInitParam(name = "studentid1", value = "2023211001000107")
        })
public class ConfigDemoServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        ServletConfig config = getServletConfig();
        // 从 web.xml 中获取参数（实际若同时配置，web.xml 会覆盖注解参数，此处仅为演示逻辑）
        String nameFromWebXml = config.getInitParameter("name");
        String studentidFromWebXml = config.getInitParameter("studentid");
//         从 @WebServlet 注解中获取参数
        String nameFromAnnotation = config.getInitParameter("name1");
        String studentidFromAnnotation = config.getInitParameter("studentid1");

        out.println("<h2>Task 1-Get init parameters from web.xml</h2>");
        out.println("<p>name " + nameFromWebXml + "</p>");
        out.println("<p>studentid " + studentidFromWebXml + "</p>");
        out.println("<h2>Task 2- Get init parameters from @WebServlet</h2>");
        out.println("<p>name1 " + nameFromAnnotation + "</p>");
        out.println("<p>studentid1 " + studentidFromAnnotation + "</p>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}