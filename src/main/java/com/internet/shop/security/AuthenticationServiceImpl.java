package com.internet.shop.security;

import com.internet.shop.exception.AuthenticationException;
import com.internet.shop.lib.Inject;
import com.internet.shop.lib.Service;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import com.internet.shop.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String login, String password) throws AuthenticationException {
        User user = userService.getByLogin(login).orElseThrow(()
                -> new AuthenticationException("Login or password is incorrect"));
        byte[] salt = user.getSalt();
        if (HashUtil.hashPassword(password, salt).equals(user.getPassword())) {
            return user;
        }
        throw new AuthenticationException("Login or password is incorrect");
    }
}
