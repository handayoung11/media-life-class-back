package com.jin.miracle.user;

import com.jin.miracle.user.dto.LogerResDTO;
import com.jin.miracle.user.dto.UpdateMeReqDTO;
import com.jin.miracle.util.CookieUtils;
import com.jin.miracle.validator.ImageFile;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String updateMe(@RequestBody @Valid UpdateMeReqDTO dto, HttpServletResponse resp) {
        String[] tokens = userSvc.updateMe(dto);
        if (tokens != null) {
            CookieUtils.configTokenCookies(tokens[0], tokens[1], resp);
            return tokens[0];
        }

        return null;
    }

    @PatchMapping(path = "me", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateProfile(@ModelAttribute @Valid @ImageFile MultipartFile profile) {
        userSvc.updateMe(profile);
    }
}
