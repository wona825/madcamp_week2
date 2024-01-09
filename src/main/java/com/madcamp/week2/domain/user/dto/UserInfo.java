package com.madcamp.week2.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {

    private Long userId;
    private String email;
    private String nickname;

    @JsonProperty("profile_img_url")
    private String profileImg;
}
