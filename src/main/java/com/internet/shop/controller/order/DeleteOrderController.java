package com.internet.shop.controller.order;

import com.internet.shop.lib.Injector;
import com.internet.shop.service.OrderService;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/orders/delete")
public class DeleteOrderController extends HttpServlet {
    private final OrderService orderService
            = (OrderService) Injector.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String id = req.getParameter("id");
        long orderId = Long.parseLong(id);
        orderService.delete(orderId);
        resp.sendRedirect(req.getContextPath() + "/orders/all");
    }
}
