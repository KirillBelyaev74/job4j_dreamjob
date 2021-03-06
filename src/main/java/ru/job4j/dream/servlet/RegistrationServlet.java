package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

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
        Store store = PsqlStore.instOf();
        if (name.equals("") || email.equals("") || password.equals("")) {
            req.setAttribute("error", "Введите все данные!");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        } else if (store.checkLiveUser(email)) {
            req.setAttribute("error", "Такой пользователь уже существует!");
            req.getRequestDispatcher("registration.jsp").forward(req, resp);
        } else {
            User user = new User(name, email, password);
            PsqlStore.instOf().addUser(user);
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
