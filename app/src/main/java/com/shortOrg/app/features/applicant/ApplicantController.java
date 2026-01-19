package com.shortOrg.app.features.applicant;

import com.shortOrg.app.shared.dto.ApplicantDto;
import com.shortOrg.app.shared.dto.ApplicantStatus;
import lombok.RequiredArgsConstructor;
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
        try {
            String userId = auth.getName();

            applicantService.applicantRequest(postId, userId);
            return ResponseEntity.ok("신청하기 성공");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 모임 신청 목록
    @GetMapping("/{postId}/applicants")
    public ResponseEntity<?> applicantShow(@PathVariable Long postId) {
        try {
            List<ApplicantDto> applicantList =  applicantService.applicantShow(postId);
            return ResponseEntity.ok(applicantList);
        } catch (Exception e) {
            throw new RuntimeException("신청 목록 불러오기 실패");
        }
    }

    // 모임 신청 처리
    @PatchMapping("/{postId}/applicants/{userId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long postId, @PathVariable String userId, @RequestBody ApplicantStatus state) {
        applicantService.updateStatus(postId, userId, state);
        return ResponseEntity.ok("변경 성공");
    }
}
