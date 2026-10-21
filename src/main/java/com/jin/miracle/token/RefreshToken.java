package com.jin.miracle.token;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID value;
    private LocalDateTime expiryDate;

    public static RefreshToken createToken() {
        RefreshToken token = new RefreshToken();
        token.generate();
        return token;
    }

    public void generate() {
        LocalDateTime now = LocalDateTime.now();
        value = UUID.randomUUID();
        expiryDate = now.plusSeconds(TokenExpiry.REFRESH_EXPIRY_SEC);
    }

    public String getValueStr() {
        return value.toString().replace("-", "");
    }
}