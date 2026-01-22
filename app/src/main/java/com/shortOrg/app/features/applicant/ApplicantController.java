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
        String userId = auth.getName();
        applicantService.applicantRequest(postId, userId);

        return ResponseEntity.ok("신청하기 성공");
    }

    // 모임 신청 목록
    @GetMapping("/{postId}/applicants")
    public ResponseEntity<?> applicantShow(@PathVariable Long postId) {
        List<ApplicantDto> applicantList =  applicantService.applicantShow(postId);
        return ResponseEntity.ok(applicantList);
}

    // 모임 신청 처리
    @PatchMapping("/{postId}/applicants/{userId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long postId, @PathVariable String userId, @RequestBody ApplicantStatus state) {
        applicantService.updateStatus(postId, userId, state);
        return ResponseEntity.ok("변경 성공");
    }
}
