package com.shortOrg.app.features.user;

import com.shortOrg.app.features.user.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> userInsert(
            @ModelAttribute SignupRequest signupRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
            ) {
        userService.userInsert(signupRequest, profileImage);
        return ResponseEntity.ok("성공");
    }

    // 프로필 이미지 업로드
    @PostMapping("")

    // 아이디 중복체크
    @GetMapping("/users/exists")
    public boolean idCheck(@RequestParam(value = "loginId") String id){
        return !userService.idCheck(id); // true면 사용가능, false면 사용불가
    }

    // 프로필 조회
    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<?> getProfile(@PathVariable("userId") String id){
        SignupRequest user = userService.userProfile(id); // 수정 해야됨

        if(user != null){
            return ResponseEntity.ok(user);
        } else {
            // not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않음");
        }
    }

    @PutMapping("users/{userId}/update")
    public ResponseEntity<?> updateProfile(@PathVariable String userId, Authentication auth) {
        String user = auth.getName();
        userService.updateProfile(userId, auth);

        return ResponseEntity.ok("수정 성공");
    }
}
