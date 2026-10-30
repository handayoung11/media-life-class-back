package com.jin.miracle.miracle.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MiracleFeedResDTO {


    List<FeedsDTO> feeds;
    int currentPage;
    boolean hasNext;


    @Getter
    @Builder
    public static class FeedsDTO {
        Long id;
        String author;
        String avatarUrl;
        String memo;
        List<String> comments;
        int mediaMin;
        int exerMin;
        int studyMin;
    }
}
