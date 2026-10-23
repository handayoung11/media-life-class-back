package com.jin.miracle.miracle.dto;

import com.jin.miracle.validator.ImageFile;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SaveMiracleReqDTO {
    String memo;

    @ImageFile
    MultipartFile mediaPic;

    @Min(value = 0, message = "미디어 사용시간에 0 이상의 수를 입력해주세요.")
    @Max(value = 1440, message = "미디어 사용시간에 1440 이하로 입력해주세요.")
    int mediaMin;

    @Min(value = 0, message = "운동시간에 0 이상의 수를 입력해주세요.")
    @Max(value = 1440, message = "운동시간에 1440 이하로 입력해주세요.")
    int exerMin;

    @Min(value = 0, message = "공부시간에 0 이상의 수를 입력해주세요.")
    @Max(value = 1440, message = "공부시간에 1440 이하로 입력해주세요.")
    int studyMin;

    String media;
    String exer;
    String study;

    @NotNull(message = "미라밸 날짜는 필수입니다.")
    LocalDate miracleDate;
}
