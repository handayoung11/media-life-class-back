package com.jin.miracle.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String nickname;
    private String profile;
    private int mediaGoal;

    @Enumerated(value = STRING)
    private Platform platform;

    @Enumerated(value = STRING)
    private UserRole role;

    @CreationTimestamp
    private LocalDate createdAt;

    public Users(String loginId, String nickname, String profile, Platform platform) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.profile = profile;
        this.platform = platform;
        role = UserRole.USER;
    }

    public void updateMediaGoal(int mediaGoal) {
        this.mediaGoal = mediaGoal;
    }
}
