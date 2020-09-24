package com.internet.shop.controller.order;

import com.internet.shop.controller.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.service.OrderService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/orders/user")
public class GetUserOrdersController extends HttpServlet {
    private final OrderService orderService
            = (OrderService) Injector.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute(LoginController.USER_ID);
        List<Order> userOrders = orderService.getUserOrders(userId);
        req.setAttribute("orders", userOrders);
        req.getRequestDispatcher("/WEB-INF/views/orders/userOrders.jsp").forward(req, resp);
    }
}
