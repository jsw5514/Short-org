package com.shortOrg.app.features.auth.dto;

public record TokenResponse(String accessToken, String refreshToken) {}