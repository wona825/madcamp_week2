package com.madcamp.week2.domain.user.service;

import com.madcamp.week2.domain.user.dto.UserInfo;
import com.madcamp.week2.domain.user.dto.ModifyUser;
import com.madcamp.week2.domain.user.entity.Follow;
import com.madcamp.week2.domain.user.entity.ProfileImg;
import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.repository.FollowRepository;
import com.madcamp.week2.domain.user.repository.UserRepository;
import com.madcamp.week2.global.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final AwsS3Uploader awsS3Uploader;

    public ModifyUser.Response modifyUser(User user, ModifyUser.Request requestBody) throws IOException {
        User foundUser = userRepository.findWithProfileImgById(user.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        if (requestBody.getNickname() != null) {
            foundUser.setNickname(requestBody.getNickname());
        }

        if (!Objects.requireNonNull(requestBody.getProfileImg().getOriginalFilename()).isEmpty()) {
            if (foundUser.getProfileImgUrl() != null) {
                foundUser.deleteProfileImage(awsS3Uploader);
            }
            foundUser.uploadProfileImage(requestBody.getProfileImg(), awsS3Uploader);
        }
        userRepository.save(foundUser);

        ProfileImg profileImg = foundUser.getProfileImg();
        return ModifyUser.Response.builder().profileImgUrl(profileImg == null ? null : profileImg.getUploadFileUrl())
                .nickname(foundUser.getNickname())
                .build();
    }

    public void deleteProfileImg(User user) {
        User foundUser = userRepository.findWithProfileImgById(user.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        if (foundUser.getProfileImgUrl() != null) {
            foundUser.deleteProfileImage(awsS3Uploader);
        }
    }

    public void followUser(User user, Long followedUserId) {
        User followedUser = userRepository.findById(followedUserId).orElseThrow(() -> new RuntimeException("팔로우 대상을 찾을 수 없습니다."));

        if (user.equals(followedUser)) {
            throw new RuntimeException("유저 본인은 팔로우할 수 없습니다.");
        }

        if (followRepository.findByFollowingAndFollowedId(user, followedUserId).isPresent()) {
            throw new RuntimeException("이미 팔로우 중인 유저입니다.");
        }

        // Follow 생성
        Follow follow = Follow.builder()
                .following(user)
                .followed(followedUser)
                .build();

        // Follow 저장
        followRepository.save(follow);
    }

    public void unFollowUser(User user, Long followedUserId) {
        followRepository.delete(followRepository.findByFollowingAndFollowedId(user, followedUserId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우 입니다.")));
    }


    public List<UserInfo> getFollowList(User user) {
        User foundUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.findByFollowingWithFollowed(foundUser);

        List<UserInfo> followList = follows.stream().map(follow ->
                    UserInfo.builder()
                            .userId(follow.getFollowed().getId())
                            .email(follow.getFollowed().getEmail())
                            .nickname(follow.getFollowed().getNickname())
                            .profileImg(follow.getFollowed().getProfileImgUrl())
                            .build()
        ).toList();

        return followList;
    }


    public List<UserInfo> getUserListBySearch(String search) {
        List<User> usersBySearch = userRepository.findBySearch(search);

        List<UserInfo> userList = usersBySearch.stream().map(userBySearch ->
                UserInfo.builder()
                        .userId(userBySearch.getId())
                        .email(userBySearch.getEmail())
                        .nickname(userBySearch.getNickname())
                        .profileImg(userBySearch.getProfileImgUrl())
                        .build()
        ).toList();

        return userList;
    }
}
