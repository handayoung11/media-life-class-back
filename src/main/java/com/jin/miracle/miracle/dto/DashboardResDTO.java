package com.jin.miracle.miracle.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DashboardResDTO {


    DashboardSummaryDTO summary;
    DashboardRecentRecordDTO today;
    List<DashboardRecentRecordDTO> recentRecords;

    @Getter
    @Builder
    public static class DashboardSummaryDTO {
        long totalRecords;
        int streakDays;
    }

    @Getter
    @Builder
    public static class DashboardRecentRecordDTO {
        LocalDate date;
        int mediaMin;
        int exerciseMin;
        int studyMin;
        String memo;
    }
}
