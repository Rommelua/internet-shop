package com.internet.shop.controller;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/registration")
public class RegistrationController extends HttpServlet {
    private final UserService userService
            = (UserService) Injector.getInstance(UserService.class);
    private final ShoppingCartService shoppingCartService
            = (ShoppingCartService) Injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String password = req.getParameter("psw");
        String passwordRep = req.getParameter("pswRep");

        if (password.equals(passwordRep)) {
            User user = new User(name, login, password, Set.of(Role.of("USER")));
            userService.create(user);
            shoppingCartService.create(new ShoppingCart(user.getId()));
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("message", "Passwords and its repeat are different, try again.");
            req.setAttribute("name", name);
            req.setAttribute("login", login);
            req.getRequestDispatcher("WEB-INF/views/registration.jsp").forward(req, resp);
        }
    }
}
