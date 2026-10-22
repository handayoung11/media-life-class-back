package com.jin.miracle.token;

import com.jin.miracle.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final TokenSvc tokenSvc;

    @PostMapping("/refresh")
    public String refresh(@CookieValue("token") Cookie accessToken,
                          @CookieValue("refresh_token") Cookie refreshToken,
                          HttpServletResponse resp) {
        String[] tokens = tokenSvc.reIssueToken(accessToken.getValue(), refreshToken.getValue());
        CookieUtils.configTokenCookies(tokens[0], tokens[1], resp);
        return tokens[0];
    }
}
