package com.jin.miracle.miracle;

import com.jin.miracle.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeLiBal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int studyMin;
    private int exerMin;
    private int mediaMin;
    private String study;
    private String exer;
    private String media;
    private String mediaPic;
    private String todayMemo;
    private LocalDate melibalDate;

    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public MeLiBal(int studyMin, int exerMin, int mediaMin, String study, String exer, String media, String todayMemo, LocalDate melibalDate, Users user) {
        this.studyMin = studyMin;
        this.exerMin = exerMin;
        this.mediaMin = mediaMin;
        this.study = study;
        this.exer = exer;
        this.media = media;
        this.todayMemo = todayMemo;
        this.melibalDate = melibalDate;
        this.user = user;
    }

    public void updateMediaPic(String mediaPic) {
        this.mediaPic = mediaPic;
    }
}
