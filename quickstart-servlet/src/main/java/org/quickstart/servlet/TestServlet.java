package org.quickstart.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/11/23 12:23
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/test"})
public class TestServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.getRequestDispatcher("index.jsp").forward(request, response);
  }
}
