package com.shortOrg.app.features.report;

import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.Report;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.repository.PostRepository;
import com.shortOrg.app.repository.ReportRepository;
import com.shortOrg.app.repository.UserRepository;
import com.shortOrg.app.features.report.dto.ReportCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    public void reportRequest(ReportCreateRequest reportRequest, String name) {
        // 신고자 조회
        User user = userRepository.findById(name).orElseThrow(() -> new RuntimeException("신고자 찾기 실패"));
        // targer 조회
        User target = userRepository.findById(reportRequest.getTargetUserId()).orElseThrow(() -> new RuntimeException("대상 찾기 실패"));
        // 신고 대상 게시글 조회
        Post post = postRepository.findById(reportRequest.getPostId()).orElseThrow(() -> new RuntimeException("게시글 찾기 실패"));


        Report report = new Report();
        report.setId(null);
        report.setReporterId(user); // Post
        report.setTargetId(target); // o User
        report.setPostId(post); // o User
        report.setDescription(reportRequest.getDescription()); // o

        reportRepository.save(report);
    }
}
