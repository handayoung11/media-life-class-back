package com.jin.miracle.config.security;

import com.jin.miracle.user.entity.Platform;
import com.jin.miracle.user.entity.Users;
import com.jin.miracle.user.repo.UserRepo;
import com.jin.miracle.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OAuth2UserSvc extends DefaultOAuth2UserService {

    private final UserRepo userRepo;

    public OAuth2UserSvc(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Users dbUser = null;

        String nameKey = "";

        try {
            if (userRequest.getClientRegistration().getRegistrationId().equals(Platform.GOOGLE.getRegId())) {
                nameKey = "sub";
                String id = oAuth2User.getAttribute(nameKey);

                dbUser = userRepo.findByLoginId(id).orElse(null);

                if (dbUser == null) {
                    String profileImage = oAuth2User.getAttribute("picture");
                    profileImage = profileImage.replace("=s96-c", "");
                    String img = FileUtils.saveImgByUrl(profileImage, id);
                    dbUser = new Users(id, oAuth2User.getAttribute("name"), img, Platform.GOOGLE);
                    userRepo.save(dbUser);
                }
            } else if (userRequest.getClientRegistration().getRegistrationId().equals(Platform.KAKAO.getRegId())) {
                nameKey = "id";
                String id = oAuth2User.getAttribute(nameKey).toString();

                dbUser = userRepo.findByLoginId(id).orElse(null);

                if (dbUser == null) {
                    Map<String, String> prs = oAuth2User.getAttribute("properties");
                    String profileImage = prs.get("profile_image");
                    String img = null;
                    if (profileImage != null) {
                        img = FileUtils.saveImgByUrl(profileImage, id);
                    }
                    dbUser = new Users(id, prs.get("nickname"), img, Platform.KAKAO);
                    userRepo.save(dbUser);
                }
            }

            return new DefaultOAuth2User(List.of(new SimpleGrantedAuthority(dbUser.getRole().toString())),
                    oAuth2User.getAttributes(), nameKey);
        } catch (Exception e) {
            // 닉네임 유니크 제약조건 위배 감지
            if (e.getMessage() != null && e.getMessage().contains("nickname_UNIQUE")) {
                log.warn("닉네임 중복 발생으로 인해 null 처리 후 회원가입을 재시도합니다. ID: {}", dbUser.getLoginId());
                try {
                    dbUser.updateNickname(null);
                    dbUser = userRepo.save(dbUser);

                    return new DefaultOAuth2User(
                            List.of(new SimpleGrantedAuthority(dbUser.getRole().toString())),
                            oAuth2User.getAttributes(),
                            nameKey
                    );
                } catch (Exception retryEx) {
                    log.error("닉네임 null 처리 후 재시도 중 에러 발생: {}", retryEx.getMessage());
                    throw new OAuth2AuthenticationException(new OAuth2Error("login_error"), retryEx.getMessage());
                }
            }
            log.error(e.getMessage());
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("login_error"),
                    e.getMessage()
            );
        }
    }
}
