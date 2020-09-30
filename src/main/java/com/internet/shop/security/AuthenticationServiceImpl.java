package com.internet.shop.security;

import com.internet.shop.exception.AuthenticationException;
import com.internet.shop.lib.Inject;
import com.internet.shop.lib.Service;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import com.internet.shop.util.HashUtil;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String login, String password) throws AuthenticationException {
        Optional<User> user = userService.getByLogin(login);
        if (user.isPresent() && HashUtil.hashPassword(password, user.get().getSalt())
                .equals(user.get().getPassword())) {
            return user.get();
        }
        throw new AuthenticationException("Login or password is incorrect");
    }
}
