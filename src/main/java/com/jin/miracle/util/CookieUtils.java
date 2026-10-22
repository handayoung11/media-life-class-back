package com.jin.miracle.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import static com.jin.miracle.token.TokenExpiry.REFRESH_EXPIRY_SEC;

public class CookieUtils {
    public static Cookie makeSecuredCookie(String key, String value, int expiry) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expiry);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "none");
        cookie.setAttribute("Partitioned", "");
        return cookie;
    }

    public static void configTokenCookies(String token, String refreshToken, HttpServletResponse resp) {
        Cookie cookie = makeSecuredCookie("token", token, REFRESH_EXPIRY_SEC);
        resp.addCookie(cookie);
        resp.addCookie(makeSecuredCookie("refresh_token", refreshToken, REFRESH_EXPIRY_SEC));
    }

    public static Cookie deleteCookie(String name) {
        Cookie cookie = makeSecuredCookie(name, null, 0);
        cookie.setMaxAge(0);
        return cookie;
    }

    public static void deleteTokens(HttpServletResponse resp) {
        resp.addCookie(deleteCookie("token"));
        resp.addCookie(deleteCookie("refresh_token"));
    }
}
