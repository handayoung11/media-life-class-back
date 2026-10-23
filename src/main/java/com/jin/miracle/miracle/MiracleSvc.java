package com.jin.miracle.miracle;

import com.jin.miracle.miracle.dto.SaveMiracleReqDTO;
import com.jin.miracle.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static void handleDuplication(Exception e) {
        if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("uk_date_user")) {
            throw makeHttpClientErrorEx(HttpStatus.UNPROCESSABLE_CONTENT, "해당 날짜에 이미 미라밸을 작성했습니다.");
        }
        throw makeHttpClientErrorEx(HttpStatus.UNPROCESSABLE_CONTENT, "DB 오류 발생");
    }
}
