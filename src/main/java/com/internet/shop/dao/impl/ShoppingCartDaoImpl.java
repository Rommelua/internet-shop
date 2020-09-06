package com.internet.shop.dao.impl;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.db.Storage;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.ShoppingCart;
import java.util.List;
import java.util.Optional;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        Storage.addShoppingCart(shoppingCart);
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getById(Long id) {
        return getAll().stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(id))
                .findFirst();
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        List<ShoppingCart> shoppingCarts = getAll();
        for (int i = 0; i < shoppingCarts.size(); i++) {
            if (shoppingCarts.get(i).getId().equals(shoppingCart.getId())) {
                shoppingCarts.set(i, shoppingCart);
                return shoppingCart;
            }
        }
        throw new IllegalArgumentException("Storage doesn't contain shoppingCart with id "
                                           + shoppingCart.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return getAll().removeIf(shoppingCart -> shoppingCart.getId().equals(id));
    }

    @Override
    public List<ShoppingCart> getAll() {
        return Storage.shoppingCarts;
    }
}
