package com.shortOrg.app.features.user.dto;

import com.shortOrg.app.shared.enumerate.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequest {
    private String id;
    private String password;
    private Gender gender;
    private LocalDate birth;
    private String nickname;
}

// 닉네임, 프로필사진, 생일, 성별