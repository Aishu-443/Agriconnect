package com.demo;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>AgriConnect Demo</title></head><body>");
        out.println("<h1 style='color:green'>Hello from AgriConnect!</h1>");
        out.println("<p>Deployed via Apache Tomcat WAR deployment.</p>");
        out.println("<p>DevOps Module 6 - Maven and Tomcat demonstration.</p>");
        out.println("</body></html>");
    }
}
