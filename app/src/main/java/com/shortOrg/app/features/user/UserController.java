package com.shortOrg.app.features.user;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.shared.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return !userService.idCheck(id); // true면 사용가능, false면 사용불가
    }

    // 프로필 조회
    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<?> getProfile(@PathVariable("userId") String id){
        User user = userService.userProfile(id);

        if(user != null){
            return ResponseEntity.ok(user);
        } else {
            // not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않음");
        }
    }
}
