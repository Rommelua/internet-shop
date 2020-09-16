package com.internet.shop.web.filter;

import com.internet.shop.controller.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private static final String LOGIN_URL = "/login";
    private static final String LOGIN_BUTTON = "Log In";
    private static final String LOGOUT_URL = "/logout";
    private static final String LOGOUT_BUTTON = "Logout";
    private static final String REGISTRATION_URL = "/registration";
    private static final String REGISTRATION_BUTTON = "Sign Up";
    private static final String USERNAME_BUTTON = "Hello, ";
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final UserService userService = (UserService) injector.getInstance(UserService.class);
    private final Set<String> allowedUrls = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowedUrls.add("/registration");
        allowedUrls.add("/login");
        allowedUrls.add("/");
        allowedUrls.add("/products/all");
        allowedUrls.add("/inject");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Long userId = (Long) req.getSession().getAttribute(LoginController.USER_ID);
        if (userId == null) {
            req.setAttribute("firstURL", LOGIN_URL);
            req.setAttribute("firstButton", LOGIN_BUTTON);
            req.setAttribute("secondURL", REGISTRATION_URL);
            req.setAttribute("secondButton", REGISTRATION_BUTTON);
            if (allowedUrls.contains(req.getServletPath())) {
                chain.doFilter(req, resp);
            } else {
                req.getRequestDispatcher("/login").forward(req, resp);
            }
        } else {
            User user = userService.get(userId);
            req.setAttribute("firstURL", "/");
            req.setAttribute("firstButton", USERNAME_BUTTON + user.getName());
            req.setAttribute("secondURL", LOGOUT_URL);
            req.setAttribute("secondButton", LOGOUT_BUTTON);
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
    }
}
