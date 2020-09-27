package com.internet.shop.controller.product;

import com.internet.shop.lib.Injector;
import com.internet.shop.service.ProductService;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/products/delete")
public class DeleteProductController extends HttpServlet {
    private final ProductService productService
            = (ProductService) Injector.getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String id = req.getParameter("id");
        long productId = Long.parseLong(id);
        productService.delete(productId);
        resp.sendRedirect(req.getContextPath() + "/products/manage");
    }
}
