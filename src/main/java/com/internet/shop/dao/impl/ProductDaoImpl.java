package com.internet.shop.dao.impl;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.db.Storage;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Dao
public class ProductDaoImpl implements ProductDao {

    @Override
    public Product create(Product product) {
        Storage.addProduct(product);
        return product;
    }

    @Override
    public Optional<Product> getById(Long productId) {
        return getAll().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    @Override
    public Product update(Product product) {
        List<Product> products = getAll();
        IntStream.range(0, products.size())
                .filter(i -> products.get(i).getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(i -> products.set(i, product), () -> {
                    throw new IllegalArgumentException("Storage doesn't contain product with id "
                                                       + product.getId());
                });
        return product;
    }

    @Override
    public boolean deleteById(Long productId) {
        return Storage.products
                .removeIf(product -> product.getId().equals(productId));
    }

    @Override
    public List<Product> getAll() {
        return Storage.products;
    }
}
