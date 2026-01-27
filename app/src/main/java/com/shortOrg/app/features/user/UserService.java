package com.shortOrg.app.features.user;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.shared.dto.ProfileRequest;
import com.shortOrg.app.features.user.dto.SignupRequest;
import com.shortOrg.app.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    // 파일 경로
    private final String uploadPath = System.getProperty("user.home")+"/shortOrg_uploads/profiles/";

    // 회원가입
    @Transactional
    public void userInsert(SignupRequest signupRequest) {
        if(idCheck(signupRequest.getId())){
            User user = new User();

            user.setId(signupRequest.getId());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setGender(signupRequest.getGender()); // F or M
            user.setBirth(signupRequest.getBirth());
            user.setNickname(signupRequest.getNickname());
            user.setAvgRate(null);
            user.setOrgTime(0L);

            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.");
        }
    }

    // 아이디 중복 확인
    public boolean idCheck(String id) {
        return !userRepository.existsById(id);
    }

    // 프로필 조회
    public ProfileRequest userProfile(String id) {
        log.info("조회하려는 아이디: {}", id);

        ProfileRequest userDto = new ProfileRequest();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("프로필 가져오기 실패"));

        userDto.setNickname(user.getNickname());
        userDto.setGender(user.getGender());
        userDto.setBirth(user.getBirth());
        userDto.setProfile(user.getProfileImage());
        userDto.setUserId(id);

        return userDto;
    }


    // 이미지 저장
    public String saveProfileImage(MultipartFile file) {
        File directory = new File(uploadPath);
        if(!directory.exists()) directory.mkdirs();

        // 파일명 생성 (UUID)
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedName = uuid + extension;

        try {
            file.transferTo(new File(uploadPath + savedName));
            return savedName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }
    }


    // 프로필 업데이트
    public void updateProfile(String userId, ProfileRequest profileRequest, MultipartFile image) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("불러오기 실패"));

        if(image != null && !image.isEmpty()) {
            String imageUrl = saveProfileImage(image);
            user.setProfileImage(imageUrl);
        }

        user.setNickname(profileRequest.getNickname());
        user.setGender(profileRequest.getGender());
        user.setBirth(profileRequest.getBirth());

        userRepository.save(user);
    }
}
