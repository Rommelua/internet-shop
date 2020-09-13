package com.internet.shop.dao.impl;

import com.internet.shop.dao.UserDao;
import com.internet.shop.db.Storage;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.User;
import java.util.List;
import java.util.Optional;

@Dao
public class UserDaoImpl implements UserDao {
    @Override
    public User create(User user) {
        Storage.addUser(user);
        return user;
    }

    @Override
    public Optional<User> getById(Long id) {
        return getAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public User update(User user) {
        List<User> users = getAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                return user;
            }
        }
        throw new IllegalArgumentException("Storage doesn't contain user with id "
                                           + user.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return getAll().removeIf(product -> product.getId().equals(id));
    }

    @Override
    public List<User> getAll() {
        return Storage.users;
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return getAll().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }
}
