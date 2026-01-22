package com.shortOrg.app.shared.dto;

public record TokenValidationResult(TokenState state, String cacheKey) {
}
