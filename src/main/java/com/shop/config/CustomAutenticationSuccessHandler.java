package com.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAutenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 특정 조건을 확인하고 다르게 리다이렉트하도록 로직을 구현
        String userType = request.getParameter("userType");

        if (userType != null && userType.equals("admin")) {
            setDefaultTargetUrl("/adminMain");
        } else {
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
