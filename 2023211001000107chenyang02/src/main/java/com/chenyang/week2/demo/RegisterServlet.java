package com.chenyang.week2.demo;

import javax. servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private String driver, url, username, password;
    private Connection conn;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        driver = context.getInitParameter("driver");
        url = context.getInitParameter("url");
        this.username = context.getInitParameter("username");
        this.password = context.getInitParameter("password");

        // 初始化数据库连接
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, this.username, this.password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("数据库连接初始化失败", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 插入数据
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO usertable (username, password) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            out.println("<h1>插入数据失败: " + e.getMessage() + "</h1>");
            return;
        }

        // 查询并显示所有数据
        out.println("<h1>用户列表</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>用户名</th><th>密码</th></tr>");
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM usertable");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td>" +
                        "<td>" + rs.getString("username") + "</td>" +
                        "<td>" + rs.getString("password") + "</td></tr>");
            }
        } catch (SQLException e) {
            out.println("<h1>查询数据失败: " + e.getMessage() + "</h1>");
        }
        out.println("</table>");
    }

    @Override
    public void destroy() {
        // 关闭数据库连接
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

