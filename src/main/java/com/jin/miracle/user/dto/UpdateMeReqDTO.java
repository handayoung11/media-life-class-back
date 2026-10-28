package com.jin.miracle.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UpdateMeReqDTO {

    @Min(value = 0, message = "미디어 목표에 0분 이상으로 입력해주세요.")
    @Max(value = 1440, message = "미디어 목표에 24시간 이하로 입력해주세요.")
    Integer mediaGoal;

    @Length(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 만들어주세요.")
    String nickname;
    Boolean removeProfile;
}
