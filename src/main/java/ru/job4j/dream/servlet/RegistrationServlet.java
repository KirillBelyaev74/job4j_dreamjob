package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlUser;
import ru.job4j.dream.store.StoreUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        StoreUser store = PsqlUser.instOf();
        if (name.equals("") || email.equals("") || password.equals("")) {
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        } else if (store.checkLiveUser(email)) {
            req.setAttribute("message", "Такой пользователь уже существует!");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        } else {
            User user = new User(name, email, password);
            PsqlUser.instOf().saveUser(user);
            req.setAttribute("message", "Регистрация прошла успешно!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
