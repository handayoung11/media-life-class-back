package com.jin.miracle.user;

import com.jin.miracle.user.dto.LogerResDTO;
import com.jin.miracle.user.dto.UpdateMeReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserSvc userSvc;

    @GetMapping("me")
    public LogerResDTO getLoger() {
        return userSvc.getLoger();
    }

    @PatchMapping("me")
    public void updateMe(@RequestBody UpdateMeReqDTO dto) {
        userSvc.updateMe(dto);
    }
}
