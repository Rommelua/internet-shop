package com.internet.shop.controller;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.ProductService;
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
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final UserService userService = (UserService) injector.getInstance(UserService.class);
    private final ProductService productService
            = (ProductService) injector.getInstance(ProductService.class);
    private final ShoppingCartService shoppingCartService
            = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        userService.create(new User("admin", "admin", "1111", Set.of(Role.of("ADMIN"))));
        userService.create(new User("User A", "userA", "1111", Set.of(Role.of("USER"))));
        userService.create(new User("User B", "userB", "1111", Set.of(Role.of("USER"))));
        userService.create(new User("User C", "userC", "1111", Set.of(Role.of("USER"))));
        productService.create(new Product("BMW 320", 50_000));
        productService.create(new Product("BMW 530", 75_000));
        productService.create(new Product("BMW X5M50D", 120_000));
        shoppingCartService.create(new ShoppingCart(2L));
        shoppingCartService.create(new ShoppingCart(3L));
        shoppingCartService.create(new ShoppingCart(4L));
        req.getRequestDispatcher("/WEB-INF/views/inject.jsp").forward(req, resp);
    }
}
