package com.madcamp.week2.domain.user.service;

import com.madcamp.week2.domain.user.dto.ModifyUser;
import com.madcamp.week2.domain.user.entity.ProfileImg;
import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.repository.UserRepository;
import com.madcamp.week2.global.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
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
}
