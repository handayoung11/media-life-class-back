package com.jin.miracle.user.dto;

import com.jin.miracle.user.entity.Users;
import lombok.Getter;

import java.time.LocalDate;

import static com.jin.miracle.file.FileType.USER_DIR;
import static com.jin.miracle.util.FileUtils.getPath;

@Getter
public class LogerResDTO {
    Long id;
    String nickname;
    String profile;
    int mediaGoal;
    LocalDate createdAt;

    public LogerResDTO(Users u) {
        this.id = u.getId();
        this.nickname = u.getNickname();
        if (u.getProfile() != null && !u.getProfile().isEmpty())
            this.profile = getPath(USER_DIR, u.getProfile());
        this.createdAt = u.getCreatedAt();
        this.mediaGoal = u.getMediaGoal();
    }
}
