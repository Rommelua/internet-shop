package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;

public class WebShopApp {
    private static Injector injector = Injector.getInstance("com.internet.shop");
    private static ProductService productService;
    private static OrderService orderService;
    private static UserService userService;
    private static ShoppingCartService shoppingCartService;

    public static void main(String[] args) {
        productService = (ProductService) injector.getInstance(ProductService.class);
        orderService = (OrderService) injector.getInstance(OrderService.class);
        shoppingCartService = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        userService = (UserService) injector.getInstance(UserService.class);

        productService.create(new Product("BMW 320", 50_000));
        productService.create(new Product("BMW 530", 75_000));
        productService.create(new Product("BMW X5M50D", 120_000));
        System.out.println("All products:");
        productService.getAll().forEach(System.out::println);
        System.out.println();

        userService.create(new User("User A", "userA", "1111"));
        userService.create(new User("User B", "userB", "1111"));
        System.out.println("All users:");
        userService.getAll().forEach(System.out::println);
        System.out.println();

        System.out.println("Adding 3 products to user A cart:");
        ShoppingCart userACart = new ShoppingCart(1L);
        ShoppingCart userBCart = new ShoppingCart(2L);
        shoppingCartService.create(userACart);
        shoppingCartService.create(userBCart);
        shoppingCartService.addProduct(userACart, productService.getById(1L));
        shoppingCartService.addProduct(userACart, productService.getById(2L));
        shoppingCartService.addProduct(userACart, productService.getById(3L));
        userACart.getProducts().forEach(System.out::println);
        System.out.println();

        System.out.println("Deleting 1 product from user A cart:");
        shoppingCartService.deleteProduct(userACart, productService.getById(3L));
        userACart.getProducts().forEach(System.out::println);
        System.out.println();

        System.out.println("Placing an order from user A cart:");
        orderService.completeOrder(userACart);
        System.out.println("user A cart (must be empty now): " + userACart.getProducts());
        System.out.println();

        System.out.println("Placing one more order for user A and user B");
        shoppingCartService.addProduct(userACart, productService.getById(3L));
        orderService.completeOrder(userACart);
        shoppingCartService.addProduct(userBCart, productService.getById(2L));
        orderService.completeOrder(userBCart);
        System.out.println("User A orders:");
        System.out.println(orderService.getUserOrders(1L));
        System.out.println("User B orders:");
        System.out.println(orderService.getUserOrders(2L));

    }
}
