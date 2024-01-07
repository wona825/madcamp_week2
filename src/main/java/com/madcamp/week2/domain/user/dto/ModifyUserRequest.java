package com.madcamp.week2.domain.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class ModifyUserRequest {
    private String nickname;
    private MultipartFile profileImg;
}
