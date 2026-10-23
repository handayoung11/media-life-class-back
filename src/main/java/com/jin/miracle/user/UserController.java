package com.jin.miracle.user;

import com.jin.miracle.user.dto.LogerResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserSvc userSvc;

    @GetMapping("me")
    public LogerResDTO getLoger() {
        return userSvc.getLoger();
    }
}
