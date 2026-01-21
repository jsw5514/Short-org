package com.shortOrg.app.shared.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileRequest {
    private String nickname;
    private Gender gender;
    private LocalDate birth;
    private String profile;
    private String userId;
}
