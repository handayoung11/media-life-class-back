package com.jin.miracle.user.file;

import lombok.Getter;

@Getter
public enum FileType {
    BASE_DIR("/ext"),
    MEDIA_DIR("media"),
    USER_DIR("user");

    private final String name;

    FileType(String name) {
        this.name = name;
    }
}
