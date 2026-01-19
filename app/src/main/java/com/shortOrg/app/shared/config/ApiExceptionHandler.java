package com.shortOrg.app.shared.config;

import com.shortOrg.app.shared.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleMediaTypeError(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(415).body(err("Not Supported Media Type", e.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(err("Internal Server Error", "서버 로그 확인할것"));
    }
    
    private ErrorResponse err(String code, String message) {
        return new ErrorResponse(code,message);
    }
}
