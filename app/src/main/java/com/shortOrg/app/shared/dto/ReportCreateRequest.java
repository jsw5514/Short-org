package com.shortOrg.app.shared.dto;

import lombok.Data;

@Data
public class ReportCreateRequest {
    String targetUserId;
    Long postId;
    String description;
}
