package com.madcamp.week2.domain.user.controller;

import com.madcamp.week2.domain.user.dto.ModifyUserRequest;
import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/modify/v1")
    public ResponseEntity<?> modifyUser(@AuthenticationPrincipal User user, @RequestParam("img") MultipartFile multipartFile, String nickname) throws IOException {
        userService.modifyUser(user, multipartFile, nickname);

        return ResponseEntity.ok().build();
    }
}
