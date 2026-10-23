package com.jin.miracle.user;

import com.jin.miracle.user.dto.LogerResDTO;
import com.jin.miracle.user.entity.Users;
import com.jin.miracle.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSvc {

    private final UserUtil userUtil;

    public LogerResDTO getLoger() {
        Users u = userUtil.getLogerOrThrow();
        return new LogerResDTO(u);
    }
}
