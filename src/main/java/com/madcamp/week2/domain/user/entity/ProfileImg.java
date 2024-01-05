package com.madcamp.week2.domain.user.entity;

import com.madcamp.week2.global.s3.FileInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_img_id")
    private Long id;

    private String originalFileName;    // 원본 파일 이름
    private String uploadFileName;      // 업로드 파일 이름
    private String uploadFilePath;      // 업로드 파일 경로
    private String uploadFileUrl;       // 업로드 파일 url

    public static ProfileImg of(FileInfo fileInfo) {
        return ProfileImg.builder()
                .originalFileName(fileInfo.getOriginalFileName())
                .uploadFileName(fileInfo.getUploadFileName())
                .uploadFilePath(fileInfo.getUploadFilePath())
                .uploadFileUrl(fileInfo.getUploadFileUrl())
                .build();
    }
}
