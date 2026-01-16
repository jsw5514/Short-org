package com.shortOrg.app.features.report;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.Report;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.repository.ReportRepository;
import com.shortOrg.app.repository.UserRepository;
import com.shortOrg.app.shared.dto.ReportCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    public void reportRequest(ReportCreateRequest reportRequest, String name) {
        User user = userRepository.findById(name).orElseThrow();
        Report report = new Report();

        report.setId(null);
        report.setReporterId(); // Post
        report.setTargetId(); // o User
        report.setPostId(); // o User
        report.setDescription(reportRequest.getDescription()); // o
        report.setCreatedAt(null);
    }
}
