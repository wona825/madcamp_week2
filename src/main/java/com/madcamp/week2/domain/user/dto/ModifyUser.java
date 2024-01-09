package com.madcamp.week2.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class ModifyUser {

    @Data
    @Builder
    public static class Request {
        private String nickname;

        @JsonProperty("profile_img")
        private MultipartFile profileImg;
    }

    @Data
    @Builder
    public static class Response {
        private String nickname;

        @JsonProperty("profile_img_url")
        private String profileImgUrl;
    }
}
