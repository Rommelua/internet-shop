package com.internet.shop.service;

import com.internet.shop.model.Product;
import java.util.List;

public interface ProductService {
    Product create(Product product);

    Product getById(Long productId);

    Product update(Product product);

    List<Product> getAll();

    boolean delete(Long id);
}
