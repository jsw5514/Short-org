package com.shortOrg.app.shared.dto;

public enum TokenState {
    VALID_ACTIVE, 
    VALID_REPLAY, 
    INVALID_REUSED, 
    INVALID_UNKNOWN
}
