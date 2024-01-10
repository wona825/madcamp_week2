package com.madcamp.week2.global.auth.social;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SocialAuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("is_registered")
    private Boolean isRegistered;

    private String nickname;
    private String email;

    @JsonProperty("profile_img")
    private String profileImg;

    @JsonProperty("walking_count")
    private Long walkingCount;
}
