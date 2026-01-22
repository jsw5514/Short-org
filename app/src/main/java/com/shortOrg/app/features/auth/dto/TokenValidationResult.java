package com.shortOrg.app.features.auth.dto;

import com.shortOrg.app.shared.enumerate.TokenState;

public record TokenValidationResult(TokenState state, String cacheKey) {
}
