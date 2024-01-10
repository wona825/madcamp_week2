package com.madcamp.week2.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FollowUserInfo {

    private Long userId;
    private String email;
    private String nickname;

    @JsonProperty("profile_img_url")
    private String profileImg;

    @JsonProperty("walking_start_date_time")
    private LocalDateTime walkingStartDateTime;
}
