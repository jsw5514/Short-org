package com.shortOrg.app.shared.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String code;
    private final String message;
}
