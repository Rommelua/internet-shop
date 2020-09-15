package com.internet.shop.web.filter;

import com.internet.shop.controller.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class AuthorisationFilter implements Filter {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final UserService userService = (UserService) injector.getInstance(UserService.class);
    private final Map<String, Set<Role.RoleName>> protectedUrls = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        protectedUrls.put("/products/manage", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/users/all", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/orders/all", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/shopping-carts/products/", Set.of(Role.RoleName.USER));
        protectedUrls.put("/shopping-carts/products/add", Set.of(Role.RoleName.USER));
        protectedUrls.put("/shopping-carts/products/delete", Set.of(Role.RoleName.USER));
        protectedUrls.put("/orders/complete", Set.of(Role.RoleName.USER));
        protectedUrls.put("/orders/user", Set.of(Role.RoleName.USER));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String requestedUrl = req.getServletPath();
        Set<Role.RoleName> allowedRoles = protectedUrls.get(requestedUrl);
        if (allowedRoles == null) {
            chain.doFilter(req, resp);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute(LoginController.USER_ID);
        User user = userService.get(userId);
        if (isAuthorised(user, allowedRoles)) {
            chain.doFilter(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isAuthorised(User user, Set<Role.RoleName> roleNames) {
        return user.getRoles().stream()
                .map(Role::getRoleName)
                .anyMatch(roleNames::contains);
    }
}
