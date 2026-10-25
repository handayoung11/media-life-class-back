package com.jin.miracle.user;

import com.jin.miracle.user.dto.LogerResDTO;
import com.jin.miracle.user.dto.UpdateMeReqDTO;
import com.jin.miracle.user.entity.Users;
import com.jin.miracle.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSvc {

    private final UserUtil userUtil;

    public LogerResDTO getLoger() {
        Users u = userUtil.getLogerOrThrow();
        return new LogerResDTO(u);
    }

    @Transactional
    public void updateMe(UpdateMeReqDTO dto) {
        Users u = userUtil.getLogerOrThrow();
        u.updateMediaGoal(dto.getMediaGoal());
    }
}
