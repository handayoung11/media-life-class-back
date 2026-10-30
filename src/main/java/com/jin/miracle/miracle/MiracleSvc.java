package com.jin.miracle.miracle;

import com.jin.miracle.file.FileType;
import com.jin.miracle.miracle.dto.DashboardResDTO;
import com.jin.miracle.miracle.dto.MiracleFeedResDTO;
import com.jin.miracle.miracle.dto.SaveMiracleReqDTO;
import com.jin.miracle.util.FileUtils;
import com.jin.miracle.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.jin.miracle.file.FileType.MEDIA_DIR;
import static com.jin.miracle.util.ExUtils.makeHttpClientErrorEx;
import static com.jin.miracle.util.FileUtils.save;

@Slf4j
@Service
@RequiredArgsConstructor
public class MiracleSvc {

    private final MiracleRepo miracleRepo;
    private final UserUtil userUtil;

    @Transactional
    public Long saveMiracle(SaveMiracleReqDTO s) {

        Miracle miracle = Miracle.builder()
                .studyMin(s.getStudyMin())
                .exerMin(s.getExerMin())
                .mediaMin(s.getMediaMin())
                .study(s.getStudy())
                .exer(s.getExer())
                .media(s.getMedia())
                .todayMemo(s.getMemo())
                .miracleDate(s.getMiracleDate())
                .user(userUtil.getLogerOrThrow())
                .build();
        try {
            miracleRepo.save(miracle);
        } catch (Exception e) {
            handleDuplication(e);
        }

        if (s.getMediaPic() != null) {
            String mediaPic = save(MEDIA_DIR, s.getMediaPic(), miracle.getId() + "");
            miracle.updateMediaPic(mediaPic);
        }

        return miracle.getId();
    }

    private void handleDuplication(Exception e) {
        if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("uk_date_user")) {
            throw makeHttpClientErrorEx(HttpStatus.UNPROCESSABLE_CONTENT, "해당 날짜에 이미 미라밸을 작성했습니다.");
        }
        throw makeHttpClientErrorEx(HttpStatus.UNPROCESSABLE_CONTENT, "DB 오류 발생");
    }

    public DashboardResDTO getDashboard() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Miracle> miracles = miracleRepo.findLatestRecords(userId);

        long total = miracleRepo.countByUser_LoginId(userId);
        int streakyDays = calculateCurrentStreak(miracles);

        DashboardResDTO.DashboardRecentRecordDTO todayRecord = null;
        List<DashboardResDTO.DashboardRecentRecordDTO> records = new ArrayList<>();

        if (!miracles.isEmpty()) {
            Miracle latestMiracle = miracles.get(0);
            if (latestMiracle.isTodayOrYesterday()) {
                todayRecord = DashboardResDTO.DashboardRecentRecordDTO.builder()
                        .date(latestMiracle.getMiracleDate())
                        .mediaMin(latestMiracle.getMediaMin())
                        .exerciseMin(latestMiracle.getExerMin())
                        .studyMin(latestMiracle.getStudyMin())
                        .memo(latestMiracle.getTodayMemo())
                        .build();
            }

            for (Miracle m : miracles) {
                records.add(DashboardResDTO.DashboardRecentRecordDTO.builder()
                        .date(m.getMiracleDate())
                        .mediaMin(m.getMediaMin())
                        .exerciseMin(m.getExerMin())
                        .studyMin(m.getStudyMin())
                        .memo(m.getTodayMemo())
                        .build());
            }
        }

        return DashboardResDTO.builder()
                .summary(DashboardResDTO.DashboardSummaryDTO.builder()
                        .totalRecords(total)
                        .streakDays(streakyDays)
                        .build())
                .today(todayRecord)
                .recentRecords(records)
                .build();
    }

    public int calculateCurrentStreak(List<Miracle> miracles) {
        if (miracles.isEmpty()) {
            return 0;
        }

        // 어제, 오늘 기록 없으면 연속기록 끊긴 것임!
        if (!miracles.get(0).isTodayOrYesterday()) {
            return 0;
        }

        LocalDate expectedDate = miracles.get(0).getMiracleDate();
        int streak = 0;

        for (Miracle miracle : miracles) {
            // 날짜가 예상한 날짜와 일치하면 streak 증가 후 다음 예상 날짜를 하루 전으로 세팅
            if (miracle.getMiracleDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            }
            else break;
        }
        return streak;
    }

    public MiracleFeedResDTO getMiracles(Pageable p) {
        Slice<Miracle> miracles = miracleRepo.findWithUser(p);
        List<MiracleFeedResDTO.FeedsDTO> feeds = miracles.stream().map(m -> MiracleFeedResDTO.FeedsDTO.builder()
                .id(m.getId())
                .author(m.getUser().getNickname())
                .avatarUrl(FileUtils.getPath(FileType.USER_DIR, m.getUser().getProfile()))
                .exerMin(m.getExerMin())
                .studyMin(m.getStudyMin())
                .mediaMin(m.getMediaMin())
                .memo(m.getTodayMemo())
                .comments(new ArrayList<>())
                .build()
        ).toList();
        return MiracleFeedResDTO.builder()
                .feeds(feeds)
                .currentPage(miracles.getNumber())
                .hasNext(miracles.hasNext())
                .build();
    }
}
