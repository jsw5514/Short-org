package com.shortOrg.app.features.applicant;

import com.shortOrg.app.features.applicant.dto.ApplicantResponse;
import com.shortOrg.app.shared.enumerate.ApplicantStatus;
import com.shortOrg.app.shared.dto.ErrorResponse;
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
        String userId = auth.getName();
        try {
            applicantService.applicantRequest(postId, userId);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 신청되어 있음");
        }

        return ResponseEntity.ok("신청하기 성공");
    }

    // 모임 신청 목록
    @GetMapping("/{postId}/applicants")
    public ResponseEntity<?> applicantShow(@PathVariable Long postId) {
        List<ApplicantResponse> applicantList =  applicantService.applicantShow(postId);
        return ResponseEntity.ok(applicantList);
}

    // 모임 신청 처리
    @PatchMapping("/{postId}/applicants/{userId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long postId, @PathVariable String userId, @RequestBody ApplicantStatus state, Authentication auth) {
        String writerId = auth.getName();
        try {
            applicantService.updateStatus(postId, userId, state, writerId);
            return ResponseEntity.ok("변경 성공");
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{postId}/applicants")
    public ResponseEntity<?> cancelApply(@PathVariable Long postId, Authentication auth) {
        try {
            applicantService.cancelApply(postId, auth.getName());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse("NOT_FOUND","모임을 찾을 수 없거나 신청되어있지 않음"));
        }
        return ResponseEntity.ok("모임 신청 취소됨");
    }
}
