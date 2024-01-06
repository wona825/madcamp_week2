package com.madcamp.week2.global.auth.social.kakao.dto;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;
    private String connected_at;
    private KakaoAccount kakao_account;

    @Data
    public static class KakaoAccount {
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;
        private Profile profile;
    }

    @Data
    public static class Profile {
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private Boolean is_default_image;
    }
}
