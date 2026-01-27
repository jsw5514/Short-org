package com.shortOrg.app.features.user;

import com.shortOrg.app.features.user.dto.SignupRequest;
import com.shortOrg.app.shared.dto.ProfileRequest;
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
    @PostMapping("/users")
    public ResponseEntity<?> userInsert(@RequestBody SignupRequest signupRequest) {
        userService.userInsert(signupRequest);
        return ResponseEntity.ok("성공");
    }

    // 아이디 중복체크
    @GetMapping("/users/exists")
    public boolean idCheck(@RequestParam(value = "loginId") String id){
        return userService.idCheck(id); // true면 사용가능, false면 사용불가
    }

    // 프로필 조회
    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<?> getProfile(@PathVariable("userId") String id){
        ProfileRequest user = userService.userProfile(id);

        if(user != null){
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않음");
        }
    }

    // 프로필 수정
    @PutMapping(value = "/users/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @RequestPart("data") ProfileRequest profileRequest, Authentication auth,
            @RequestPart(value = "image", required = false) MultipartFile image
        ) {
        String user = auth.getName();
        if(profileRequest.getUserId().equals(user)){
            userService.updateProfile(user, profileRequest, image);
        } else {
            throw new RuntimeException("실패");
        }

        return ResponseEntity.ok("수정 성공");
    }
}

