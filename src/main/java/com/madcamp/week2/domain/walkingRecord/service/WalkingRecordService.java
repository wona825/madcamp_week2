package com.madcamp.week2.domain.walkingRecord.service;

import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.repository.FollowRepository;
import com.madcamp.week2.domain.walkingRecord.dto.WalkingRecordInfo;
import com.madcamp.week2.domain.walkingRecord.entity.Location;
import com.madcamp.week2.domain.walkingRecord.entity.WalkingRecord;
import com.madcamp.week2.domain.walkingRecord.repository.LocationRepository;
import com.madcamp.week2.domain.walkingRecord.repository.WalkingRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkingRecordService {

    private final FollowRepository followRepository;
    private final WalkingRecordRepository walkingRecordRepository;
    private final LocationRepository locationRepository;

    public void saveWalkingRecord(User user, WalkingRecordInfo walkingRecordInfo) {

        WalkingRecord walkingRecord = WalkingRecord.builder()
                .user(user)
                .totalWalkingTime(walkingRecordInfo.getTotalWalkingTime())
                .walkingStartDateTime(walkingRecordInfo.getWalkingStartDateTime())
                .walkingDistance(walkingRecordInfo.getWalkingDistance())
                .build();

        List<Location> locationList = walkingRecordInfo.getLocationList().stream().map(location ->
                Location.builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .recordOrder(location.getRecordOrder())
                        .walkingRecord(walkingRecord)
                        .build()
        ).collect(Collectors.toList());

        locationRepository.saveAll(locationList);
        walkingRecordRepository.save(walkingRecord);
    }

    public List<WalkingRecordInfo> getWalkingRecordList(User user, Long followedUserId) {

        if (followRepository.findByFollowingAndFollowedId(user, followedUserId).isEmpty()) {
            throw new RuntimeException("팔로우하지 않는 대상입니다.");
        }

        List<WalkingRecord> walkingRecords = walkingRecordRepository.findByUserId(followedUserId);


        List<WalkingRecordInfo> walkingRecordInfos = walkingRecords.stream().map(record ->
                WalkingRecordInfo.builder()
                        .walkingRecordId(record.getId())
                        .walkingDistance(record.getWalkingDistance())
                        .walkingStartDateTime(record.getWalkingStartDateTime())
                        .totalWalkingTime(record.getTotalWalkingTime())
                        .locationList(record.getLocations().stream().map(location ->
                                WalkingRecordInfo.Location.builder()
                                        .recordOrder(location.getRecordOrder())
                                        .longitude(location.getLongitude())
                                        .latitude(location.getLatitude())
                                        .build()
                        ).toList()
                        ).build()
        ).toList();

        return walkingRecordInfos;
    }

    public List<WalkingRecordInfo> getMyWalkingRecordList(User user) {

        List<WalkingRecord> walkingRecords = walkingRecordRepository.findByUserId(user.getId());

        List<WalkingRecordInfo> walkingRecordInfos = walkingRecords.stream().map(record ->
                WalkingRecordInfo.builder()
                        .walkingRecordId(record.getId())
                        .walkingDistance(record.getWalkingDistance())
                        .walkingStartDateTime(record.getWalkingStartDateTime())
                        .totalWalkingTime(record.getTotalWalkingTime())
                        .locationList(record.getLocations().stream().map(location ->
                                        WalkingRecordInfo.Location.builder()
                                                .recordOrder(location.getRecordOrder())
                                                .longitude(location.getLongitude())
                                                .latitude(location.getLatitude())
                                                .build()
                                ).toList()
                        ).build()
        ).toList();

        return walkingRecordInfos;
    }

    public void deleteWalkingRecord(User user, Long walkingRecordId) {
        WalkingRecord walkingRecord = walkingRecordRepository.findById(walkingRecordId).orElseThrow(() -> new RuntimeException("해당 산책 기록을 찾을 수 없습니다."));

        if (!walkingRecord.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("본인이 아닌 것으로 확인되어 삭제 권한이 없습니다.");
        }

        walkingRecordRepository.delete(walkingRecord);
    }
}
