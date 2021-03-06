package com.internet.shop.controller.order;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.service.OrderService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/orders/details")
public class ShowOrderController extends HttpServlet {
    private final OrderService orderService
            = (OrderService) Injector.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        long orderId = Long.parseLong(id);
        Order order = orderService.get(orderId);
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/orders/showOrder.jsp").forward(req, resp);
    }
}
