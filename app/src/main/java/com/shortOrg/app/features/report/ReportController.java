package com.shortOrg.app.features.report;

import com.shortOrg.app.shared.dto.ReportCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/reports")
@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("")
    public ResponseEntity<?> reportRequest(@RequestBody ReportCreateRequest reportRequest, Authentication auth) {
        String name = auth.getName();
        reportService.reportRequest(reportRequest, name);

        return ResponseEntity.ok("");
    }
}
