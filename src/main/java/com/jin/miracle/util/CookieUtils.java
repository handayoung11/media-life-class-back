package com.jin.miracle.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import static com.jin.miracle.token.TokenExpiry.REFRESH_EXPIRY_SEC;

public class CookieUtils {
    private static boolean secured = true;

    public static Cookie makeSecuredCookie(String key, String value, int expiry) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expiry);
        cookie.setSecure(secured);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "none");
        cookie.setAttribute("Partitioned", "");
        return cookie;
    }

    public static void configTokenCookies(String token, String refreshToken, HttpServletResponse resp) {
        Cookie cookie = makeSecuredCookie("token", token, REFRESH_EXPIRY_SEC);
        cookie.setHttpOnly(false);
        resp.addCookie(cookie);
        resp.addCookie(makeSecuredCookie("refresh_token", refreshToken, REFRESH_EXPIRY_SEC));
    }
}
