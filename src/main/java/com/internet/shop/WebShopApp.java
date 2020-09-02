package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ProductServiceImpl;

public class WebShopApp {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        //ProductService productService = new ProductServiceImpl();
        productService.create(new Product("BMW 320", 50_000));
        productService.create(new Product("BMW 530", 75_000));
        productService.create(new Product("BMW X5M50D", 120_000));
        System.out.println("All products:");
        productService.getAll().forEach(System.out::println);

        System.out.println("Changing name from BMW 530 to BMW 530D:");
        Product product = productService.getById(2l);
        product.setName("BMW 530D");
//        productService.update(greenTeaProduct);
//        System.out.println(productService.getById(GREEN_TEA_ID));
//        System.out.println("Deleting coffee:");
//        productService.deleteById(COFFEE_ID);
//        productService.getAll().forEach(System.out::println);
    }
}
