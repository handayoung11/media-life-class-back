package com.jin.miracle.user;

import com.jin.miracle.file.FileType;
import com.jin.miracle.user.dto.LogerResDTO;
import com.jin.miracle.user.dto.UpdateMeReqDTO;
import com.jin.miracle.user.entity.Users;
import com.jin.miracle.util.FileUtils;
import com.jin.miracle.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        if (dto.getMediaGoal() != null) u.updateMediaGoal(dto.getMediaGoal());
        if (dto.getNickname() != null) u.updateNickname(dto.getNickname());
        if (dto.getRemoveProfile() != null && dto.getRemoveProfile()) u.updateProfile(null);
    }

    @Transactional
    public void updateMe(MultipartFile file) {
        Users u = userUtil.getLogerOrThrow();
        if (file != null) {
            String name = FileUtils.save(FileType.USER_DIR, file, u.getLoginId());
            u.updateProfile(name);
        }
    }
}
