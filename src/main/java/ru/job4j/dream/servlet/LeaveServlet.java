package ru.job4j.dream.servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LeaveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().setAttribute("user", null);
        resp.sendRedirect(req.getContextPath() + "/index.do");
    }
}
