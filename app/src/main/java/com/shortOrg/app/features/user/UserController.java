package com.shortOrg.app.features.user;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.user.dto.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> userInsert(@RequestBody SignupRequest signupRequest) {
        try {
            userService.userInsert(signupRequest);
            return ResponseEntity.ok("성공");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실패");
        }
    }

    @GetMapping("/users/exists")
    public boolean idCheck(@RequestParam(value = "loginId") String id){
        return !userService.idCheck(id);
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<?> getProfile(@PathVariable("userId") String id){
        try {
            User user = userService.userProfile(id);

            if(user != null){
                return ResponseEntity.ok(user);
            } else {
                // not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않음");
            }

        } catch (Exception e){
            // 서버 내부 오류 발생
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실패");
        }
    }
}
