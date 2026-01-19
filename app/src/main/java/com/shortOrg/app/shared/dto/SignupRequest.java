package com.shortOrg.app.shared.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequest {
    private String id;
    private String password;
    private Gender gender;
    private LocalDate birthday;
}
