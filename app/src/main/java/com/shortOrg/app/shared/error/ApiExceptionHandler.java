package com.shortOrg.app.shared.error;

import com.shortOrg.app.shared.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleMediaTypeError(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.status(415).body(err("Not Supported Media Type", e.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(err("Not Supported Argument Type", e.getMessage()));
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(405).body(err("Not Supported Method Type", e.getMessage()));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<?> handleServletRequestBindingException(ServletRequestBindingException e) {
        return ResponseEntity.badRequest().body(err("Invalid Request Content", e.getMessage()));
    }
    
    @ExceptionHandler(TokenReusedException.class)
    public ResponseEntity<?> handleTokenReused(TokenReusedException e) {
        return ResponseEntity.status(401).body(err("Unauthorized", "Invalid Refresh Token. Please login again."));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(err("Internal Server Error", "서버 로그를 확인해주세요."));
    }
    
    private ErrorResponse err(String code, String message) {
        return new ErrorResponse(code,message);
    }
}
