package com.madcamp.week2.global.auth.social.kakao.service;

import com.madcamp.week2.domain.user.entity.ProfileImg;
import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.repository.UserRepository;
import com.madcamp.week2.global.auth.local.data.Token;
import com.madcamp.week2.global.auth.local.data.TokenType;
import com.madcamp.week2.global.auth.local.repository.TokenRepository;
import com.madcamp.week2.global.auth.social.SocialAuthenticationResponse;
import com.madcamp.week2.global.auth.social.kakao.dto.KakaoUserInfo;
import com.madcamp.week2.global.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomKakaoService {
    private final WebClient webClient;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    private final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    public SocialAuthenticationResponse authenticate(KakaoUserInfo.KakaoAccount kakaoAccount) {
        // 이메일이 없을때
        if (kakaoAccount.getEmail() == null) {
            throw new IllegalArgumentException("카카오 유저 정보를 찾을 수 없습니다.");
        }

        // 유저가 존재하지 않으면 생성
        Optional<User> optionalUser = userRepository.findByEmail(kakaoAccount.getEmail());

        String email = kakaoAccount.getEmail();
        String nickname = kakaoAccount.getProfile().getNickname();
        KakaoUserInfo.Profile profile = kakaoAccount.getProfile();

        String profileImg = profile.getProfile_image_url() == null ? null : profile.getProfile_image_url();
        ProfileImg profileImg1 = ProfileImg.builder().originalFileName(profileImg).build();


        User user;
        boolean isRegistered = true;
        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .profileImg(profileImg1)
                    .password(null)
                    .walkingRecords(Collections.emptyList())
                    .build();
            userRepository.save(user);
            isRegistered = false;
        } else {
            user = optionalUser.get();
        }

        // 토큰 생성
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 기존 토큰 만료
        revokeAllUserTokens(user);

        // 토큰 저장
        saveUserToken(user, jwtToken);

        ProfileImg profileImg2 = user.getProfileImg();


        return SocialAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImg(profileImg2 == null ? null : profileImg2.getUploadFileUrl())
                .isRegistered(isRegistered)
                .walkingCount(user.getWalkingRecords().stream().map(
                        record -> record.getWalkingStartDateTime().isAfter(LocalDateTime.now().minusDays(7))
                ).count())
                .build();
    }

    public KakaoUserInfo getKakaoInfoByToken(String token) {
        return webClient.get()
                .uri(KAKAO_USER_INFO_URI)
                .header("Authorization", "Bearer " + token)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
