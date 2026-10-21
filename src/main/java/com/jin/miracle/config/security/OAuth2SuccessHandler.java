package com.jin.miracle.config.security;

import com.jin.miracle.token.TokenSvc;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.jin.miracle.util.CookieUtils.configTokenCookies;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${spring.front:http://localhost:5173}")
    String front;

    private final TokenSvc tokenSvc;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
        String[] tokens = tokenSvc.generateToken(authentication);
        configTokenCookies(tokens[0], tokens[1], resp);
        String targetUrl = UriComponentsBuilder.fromUriString(front + "/login/callback")
                .queryParam("token", tokens[0])
                .build().toUriString();
        resp.sendRedirect(targetUrl);
    }
}
