package com.shortOrg.app.features.auth.dto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;

public record JwtResult(String token, Header header, Claims claims) {
}
