package com.madcamp.week2.domain.walkingRecord.controller;

import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.walkingRecord.dto.WalkingRecordInfo;
import com.madcamp.week2.domain.walkingRecord.service.WalkingRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/walkingRecord")
@RequiredArgsConstructor
public class WalkingRecordController {

    private final WalkingRecordService walkingRecordService;

    /**
     * 산책 기록을 저장하는 함수
     * @param user 산책 기록 저장을 요청한 사용자
     * @param walkingRecordInfo 산책 기록의 세부 info
     * @return
     */
    @PostMapping("/save/v1")
    public ResponseEntity<?> saveWalkingRecord(@AuthenticationPrincipal User user, @RequestBody WalkingRecordInfo walkingRecordInfo) {

        walkingRecordService.saveWalkingRecord(user, walkingRecordInfo);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/list/v1")
    public ResponseEntity<?> getWalkingRecordList(@AuthenticationPrincipal User user, @RequestParam Long followedUserId) {

        List<WalkingRecordInfo> walkingRecordInfos = walkingRecordService.getWalkingRecordList(user, followedUserId);
        return ResponseEntity.ok(walkingRecordInfos);
    }

    @GetMapping("/my-list/v1")
    public ResponseEntity<?> getMyWalkingRecordList(@AuthenticationPrincipal User user) {

        List<WalkingRecordInfo> walkingRecordInfos = walkingRecordService.getMyWalkingRecordList(user);
        return ResponseEntity.ok(walkingRecordInfos);
    }

    @DeleteMapping("/delete/v1")
    public ResponseEntity<?> deleteWalkingRecord(@AuthenticationPrincipal User user, @RequestParam Long walkingRecordId) {

        walkingRecordService.deleteWalkingRecord(user, walkingRecordId);
        return ResponseEntity.ok(null);
    }
}

