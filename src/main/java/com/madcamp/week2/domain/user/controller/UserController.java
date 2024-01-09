package com.madcamp.week2.domain.user.controller;

import com.madcamp.week2.domain.user.dto.UserInfo;
import com.madcamp.week2.domain.user.dto.ModifyUser;
import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/delete/profileImg/v1")
    public ResponseEntity<?> deleteProfileImg(@AuthenticationPrincipal User user) {

        userService.deleteProfileImg(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/follow/v1")
    public ResponseEntity<?> followUser(@AuthenticationPrincipal User user, @RequestParam Long followedUserId) {

        userService.followUser(user, followedUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unFollow/v1")
    public ResponseEntity<?> unFollowUser(@AuthenticationPrincipal User user, @RequestParam Long followedUserId) {

        userService.unFollowUser(user, followedUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/follow/list/v1")
    public ResponseEntity<?> getFollowerList(@AuthenticationPrincipal User user) {

        List<UserInfo> followList = userService.getFollowList(user);
        return ResponseEntity.ok(followList);
    }

    @GetMapping("/search/v1")
    public ResponseEntity<?> getUserListBySearch(@AuthenticationPrincipal User user, @RequestParam(required = false) String search) {

        List<UserInfo> userList = userService.getUserListBySearch(search);
        return ResponseEntity.ok(userList);
    }
}
