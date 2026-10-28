package com.jin.miracle.user;

import com.jin.miracle.user.dto.LogerResDTO;
import com.jin.miracle.user.dto.UpdateMeReqDTO;
import com.jin.miracle.validator.ImageFile;
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
    public void updateMe(@RequestBody @Valid UpdateMeReqDTO dto) {
        userSvc.updateMe(dto);
    }

    @PatchMapping(path = "me", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateProfile(@ModelAttribute @Valid @ImageFile MultipartFile profile) {
        userSvc.updateMe(profile);
    }
}
