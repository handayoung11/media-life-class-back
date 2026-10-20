package com.jin.miracle.user.entity;

import lombok.Getter;

@Getter
public enum Platform {
    KAKAO("kakao"), GOOGLE("google");

    private final String regId;

    Platform(String regId) {
        this.regId = regId;
    }
}
