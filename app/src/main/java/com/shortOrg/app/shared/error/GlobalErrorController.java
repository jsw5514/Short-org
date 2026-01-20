package com.shortOrg.app.shared.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class GlobalErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> error(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Throwable ex = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        int httpStatus = (status != null ? status : 500);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", httpStatus);
        body.put("path", path);
        body.put("message", (message != null && !message.isBlank()) ? message : "Error");
        body.put("exception", ex != null ? ex.getClass().getName() : null);
        body.put("exceptionMessage", ex != null ? ex.getMessage() : null);

        return ResponseEntity.status(httpStatus).body(body);
    }
}

