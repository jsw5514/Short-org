package com.shortOrg.app.features.report.dto;

import lombok.Data;

@Data
public class ReportCreateRequest {
    String targetUserId;
    Long postId;
    String description;
}
