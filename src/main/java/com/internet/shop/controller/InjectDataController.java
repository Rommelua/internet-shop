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

@WebServlet("/inject")
public class InjectDataController extends HttpServlet {
    private final UserService userService
            = (UserService) Injector.getInstance(UserService.class);
    private final ShoppingCartService shoppingCartService
            = (ShoppingCartService) Injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        userService.create(new User("admin", "admin", "1111", Set.of(Role.of("ADMIN"))));
        userService.create(new User("User A", "userA", "1111", Set.of(Role.of("USER"))));
        userService.create(new User("User B", "userB", "1111", Set.of(Role.of("USER"))));
        userService.create(new User("User C", "userC", "1111", Set.of(Role.of("USER"))));
        shoppingCartService.create(new ShoppingCart(3L));
        shoppingCartService.create(new ShoppingCart(4L));
        shoppingCartService.create(new ShoppingCart(5L));
        req.getRequestDispatcher("/WEB-INF/views/inject.jsp").forward(req, resp);
    }
}
