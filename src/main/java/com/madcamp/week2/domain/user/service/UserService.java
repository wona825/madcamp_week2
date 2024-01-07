package com.madcamp.week2.domain.user.service;

import com.madcamp.week2.domain.user.entity.User;
import com.madcamp.week2.domain.user.repository.UserRepository;
import com.madcamp.week2.global.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AwsS3Uploader awsS3Uploader;

    public void modifyUser(User user, MultipartFile multipartFile, String nickname) throws IOException {

        User foundUser = userRepository.findWithProfileImgById(user.getId()).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        if (nickname != null) {
            foundUser.setNickname(nickname);
        }

        if (multipartFile != null) {
            if (foundUser.getProfileImgUrl() != null) {
                foundUser.deleteProfileImage(awsS3Uploader);
            }
            foundUser.uploadProfileImage(multipartFile, awsS3Uploader);
        }
        userRepository.save(foundUser);
    }
}
