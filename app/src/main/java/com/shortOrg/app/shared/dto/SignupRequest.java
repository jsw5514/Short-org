package com.shortOrg.app.shared.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequest {
    private String id;
    private String password;
    private Gender gender;
    private LocalDate birth;
    private String nickname;
    private String profileImage;
}

// 닉네임, 프로필사진, 생일, 성별