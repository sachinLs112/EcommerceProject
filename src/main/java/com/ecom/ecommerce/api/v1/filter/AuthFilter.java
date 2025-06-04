package com.ecom.ecommerce.api.v1.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.*;
import java.io.IOException;

public class AuthFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        boolean isLoggedIn = req.getSession().getAttribute("user") != null;

        boolean isLoginRequest = uri.endsWith("login.jsp") || uri.endsWith("login") ||
                uri.endsWith("signup.jsp") || uri.endsWith("signup") ||
                uri.contains("css") || uri.contains("js");

        if (isLoggedIn || isLoginRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect("jsp/login.jsp");
        }
    }
}
