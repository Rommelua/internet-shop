package com.internet.shop.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    T create(T product);

    Optional<T> getById(Long productId);

    T update(T product);

    boolean deleteById(Long productId);

    List<T> getAll();
}
