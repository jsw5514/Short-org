package com.shortOrg.app.features.user;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.shared.dto.ProfileRequest;
import com.shortOrg.app.shared.dto.SignupRequest;
import com.shortOrg.app.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        User user = new User();

        user.setId(signupRequest.getId());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setGender(signupRequest.getGender()); // F or M
        user.setBirth(signupRequest.getBirth());
        user.setNickname(signupRequest.getNickname());
        user.setAvgRate(null);
        user.setOrgTime(0L);
        userRepository.save(user);
    }

    // 아이디 중복 확인
    public boolean idCheck(String id) {
        return userRepository.existsById(id);
    }

    // 프로필 조회
    public ProfileRequest userProfile(String id) {
        ProfileRequest userDto = new ProfileRequest();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("프로필 가져오기 실패"));

        userDto.setNickname(user.getNickname());
        userDto.setGender(user.getGender());
        userDto.setBirth(user.getBirth());
        userDto.setProfile(user.getProfileImage());
        userDto.setUserId(id);

        return userDto;
    }

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


    public void updateProfile(String userId, ProfileRequest profileRequest) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("불러오기 실패"));

        user.setId(profileRequest.getUserId());
        user.setNickname(profileRequest.getNickname());
        user.setGender(profileRequest.getGender());
        user.setProfileImage(profileRequest.getProfile());
        user.setBirth(profileRequest.getBirth());

        userRepository.save(user);
    }
}
