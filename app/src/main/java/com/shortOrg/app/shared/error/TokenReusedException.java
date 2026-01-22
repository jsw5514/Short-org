package com.shortOrg.app.shared.error;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.BadCredentialsException;

public class TokenReusedException extends BadCredentialsException {
    public TokenReusedException(@Nullable String msg) {
        super(msg);
    }

    public TokenReusedException(@Nullable String msg, Throwable cause) {
        super(msg, cause);
    }
}
