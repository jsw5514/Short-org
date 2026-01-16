package com.shortOrg.app.features.applicant;

import com.shortOrg.app.domain.Applicant;
import com.shortOrg.app.domain.Post;
import com.shortOrg.app.domain.User;
import com.shortOrg.app.shared.dto.ApplicantDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;

    // 모임 신청
    @PostMapping("/{postId}/applicants")
    public ResponseEntity<?> applicantRequest(@PathVariable Long postId, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요한 서비스입니다.");
        }

        try {
            String userId = auth.getName();

            applicantService.applicantRequest(postId, userId);
            return ResponseEntity.ok("신청하기 성공");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 신청 목록
    @GetMapping("/{postId}/applicant")
    public ResponseEntity<?> applicantShow(@PathVariable Post postId) {
        try {
            List<ApplicantDto> applicantList =  applicantService.applicantShow(postId);
            return ResponseEntity.ok(applicantList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
