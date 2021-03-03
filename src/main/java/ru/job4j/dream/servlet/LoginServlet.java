package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email.equals("") || password.equals("")) {
            req.setAttribute("error", "Введите все данные");
            req.getRequestDispatcher("login.jsp").forward(req, res);
        } else {
            User user = PsqlStore.instOf().getUser(email, password);
            req.getSession().setAttribute("user", user);
            res.sendRedirect(req.getContextPath() + "/index.do");
        }
    }
}
