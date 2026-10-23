package com.jin.miracle.util;

import com.jin.miracle.user.entity.Users;
import com.jin.miracle.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepo userRepo;

    public Users getLogerOrNull() {
        if (!isLoggedIn()) return null;
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByLoginId(loginId).orElse(null);
    }

    public Users getLogerOrThrow() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId));
    }

    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}