package com.jin.miracle.token;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenSvc {

    private final JwtEncoder encoder;
    private final RefreshTokenRepo refreshTokenRepo;

    public String[] generateToken(Authentication auth) {
        String roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        RefreshToken refreshToken = generateRefreshToken();
        return new String[] {generateToken(roles, auth.getName(), refreshToken), refreshToken.getValueStr()};
    }

    private String generateToken(String roles, String userId, RefreshToken token) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .expiresAt(now.plusSeconds(TokenExpiry.JWT_EXPIRY_SEC))
                .id(token.getId().toString())
                .subject(userId)
                .claim("rls", roles)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public RefreshToken generateRefreshToken() {
        RefreshToken token = RefreshToken.createToken();
        refreshTokenRepo.save(token);
        return token;
    }
}
