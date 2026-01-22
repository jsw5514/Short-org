package com.shortOrg.app.features.applicant.dto;


import com.shortOrg.app.shared.enumerate.ApplicantStatus;
import lombok.Data;

@Data
public class ApplicantDto {
    Long postId;
    String userId;
    ApplicantStatus state;
}
