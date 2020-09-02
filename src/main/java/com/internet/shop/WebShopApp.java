package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;

public class WebShopApp {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);

        productService.create(new Product("BMW 320", 50_000));
        productService.create(new Product("BMW 530", 75_000));
        productService.create(new Product("BMW X5M50D", 120_000));
        System.out.println("All products:");
        productService.getAll().forEach(System.out::println);
        System.out.println();

        System.out.println("Changing name from BMW 530 to BMW 530D:");
        Product productToUpdate = new Product("BMW 530D", 75_000);
        productToUpdate.setId(2L);
        productService.update(productToUpdate);
        productService.getAll().forEach(System.out::println);
        System.out.println();

        System.out.println("Deleting id = 1:");
        productService.deleteById(1L);
        productService.getAll().forEach(System.out::println);
    }
}
