package com.madcamp.week2.domain.user.controller;

import com.madcamp.week2.domain.user.dto.ModifyUser;
import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/modify/v1")
    public ResponseEntity<?> modifyUser(@AuthenticationPrincipal User user, @ModelAttribute ModifyUser.Request requestBody) throws IOException {

        ModifyUser.Response modifyUser = userService.modifyUser(user, requestBody);
        return ResponseEntity.ok(modifyUser);
    }
}
