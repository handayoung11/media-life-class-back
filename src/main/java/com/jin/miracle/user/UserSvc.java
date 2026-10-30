package com.jin.miracle.user;

import com.jin.miracle.file.FileType;
import com.jin.miracle.token.TokenSvc;
import com.jin.miracle.user.dto.LogerResDTO;
import com.jin.miracle.user.dto.UpdateMeReqDTO;
import com.jin.miracle.user.entity.UserRole;
import com.jin.miracle.user.entity.Users;
import com.jin.miracle.util.FileUtils;
import com.jin.miracle.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSvc {

    private final UserUtil userUtil;
    private final TokenSvc tokenSvc;

    public LogerResDTO getLoger() {
        Users u = userUtil.getLogerOrThrow();
        return new LogerResDTO(u);
    }

    @Transactional
    public String[] updateMe(UpdateMeReqDTO dto) {
        Users u = userUtil.getLogerOrThrow();

        if (dto.getMediaGoal() != null) u.updateMediaGoal(dto.getMediaGoal());
        if (dto.getNickname() != null) u.updateNickname(dto.getNickname());
        if (dto.getRemoveProfile() != null && dto.getRemoveProfile()) u.updateProfile(null);

        if (u.getRole().equals(UserRole.GUEST)) {
            if (u.getNickname() == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "닉네임을 입력해주세요.");
            }

            u.completeOnboarding();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    auth.getPrincipal(),
                    auth.getCredentials(),
                    List.of(new SimpleGrantedAuthority(u.getRole().toString()))
            );
            return tokenSvc.generateToken(newAuth);
        }
        return null;
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
