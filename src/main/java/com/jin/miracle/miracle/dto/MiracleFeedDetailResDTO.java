package com.jin.miracle.miracle.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MiracleFeedDetailResDTO {

    Long id;
    String author;
    String avatarUrl;
    String memo;
    String media;
    String exer;
    String study;
    String mediaPic;
    List<String> comments;
    int mediaMin;
    int exerMin;
    int studyMin;
    long likes;
    boolean liked;
    LocalDateTime createdAt;

    MiracleFeedDetailResDTO(Long id, String author, String avatarUrl, String memo,
                            String media, String exer, String study, String mediaPic, List<String> comments,
                            int mediaMin, int exerMin, int studyMin, long likes, boolean liked,
                            LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.avatarUrl = avatarUrl;
        this.memo = memo;
        this.media = media;
        this.exer = exer;
        this.study = study;
        this.mediaPic = mediaPic;
        this.comments = comments;
        this.mediaMin = mediaMin;
        this.exerMin = exerMin;
        this.studyMin = studyMin;
        this.likes = likes;
        this.liked = liked;
        this.createdAt = createdAt;
    }
}
