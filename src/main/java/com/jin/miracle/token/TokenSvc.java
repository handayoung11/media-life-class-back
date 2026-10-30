package com.jin.miracle.token;

import com.jin.miracle.exception.DeleteTokensEx;
import com.jin.miracle.exception.LogoutErrRes;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

import static com.jin.miracle.util.ExUtils.makeHttpClientErrorEx;

@Service
public class TokenSvc {

    private final JwtEncoder encoder;
    private final RefreshTokenRepo refreshTokenRepo;
    private final NimbusJwtDecoder decoder;
    private final JwtTimestampValidator jwtValidator = new JwtTimestampValidator(Duration.ofSeconds(30));
    private final String authClaimName = "rls";


    public TokenSvc(JwtEncoder encoder, RefreshTokenRepo refreshTokenRepo, @Value("${jwt.public.key}") RSAPublicKey key) {
        this.encoder = encoder;
        this.refreshTokenRepo = refreshTokenRepo;
        decoder = NimbusJwtDecoder.withPublicKey(key).build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>());
    }

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
                .claim(authClaimName, roles)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public RefreshToken generateRefreshToken() {
        RefreshToken token = RefreshToken.createToken();
        refreshTokenRepo.save(token);
        return token;
    }

    @Transactional
    public String[] reIssueToken(String token, String refreshToken) {
        Jwt jwt;
        try {
            jwt = decoder.decode(token);
        } catch (JwtException e) {
            throw new DeleteTokensEx(makeHttpClientErrorEx(HttpStatus.UNAUTHORIZED, new LogoutErrRes("jwt is not valid")));
        }

        if (!jwtValidator.validate(jwt).hasErrors()) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_CONTENT, "jwt is valid");
        }

        String id = jwt.getId();
        RefreshToken newRefreshToken = refreshTokenRepo.findByValueAndId(refreshToken, id)
                .orElseThrow(() -> new DeleteTokensEx(makeHttpClientErrorEx(HttpStatus.UNAUTHORIZED, new LogoutErrRes("refreshToken is not valid"))));
        if (newRefreshToken.isExpired()) {
            refreshTokenRepo.deleteByValue(refreshToken);
            throw new DeleteTokensEx(makeHttpClientErrorEx(HttpStatus.UNAUTHORIZED, new LogoutErrRes("refreshtoken is expired")));
        }

        newRefreshToken.generate();
        String newToken = generateToken(jwt.getClaimAsString(authClaimName), jwt.getSubject(), newRefreshToken);
        return new String[] {newToken, newRefreshToken.getValueStr()};
    }
}
