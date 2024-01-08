package com.madcamp.week2.domain.user.service;

import com.madcamp.week2.domain.user.dto.FollowedUser;
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

    public void followUser(User user, String followedEmail) {
        User followingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        User followedUser = userRepository.findByEmail(followedEmail).orElseThrow(() -> new RuntimeException("팔로우 대상을 찾을 수 없습니다."));

        if (followingUser == followedUser) {
            throw new RuntimeException("유저 본인은 팔로우할 수 없습니다.");
        }

        if (followRepository.findByFollowingAndFollowed(followingUser, followedUser).isPresent()) {
            throw new RuntimeException("이미 팔로우 중인 유저입니다.");
        }

        // Follow 생성
        Follow follow = Follow.builder()
                .following(followingUser)
                .followed(followedUser)
                .build();

        // Follow 저장
        followRepository.save(follow);
    }


    public List<FollowedUser> getFollowList(User user) {
        User foundUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.findByFollowingWithFollowed(foundUser);

        List<FollowedUser> followList = follows.stream().map(follow ->
                    FollowedUser.builder()
                            .email(follow.getFollowed().getEmail())
                            .nickname(follow.getFollowed().getNickname())
                            .profileImg(follow.getFollowed().getProfileImgUrl())
                            .build()
        ).toList();

        return followList;
    }
}
