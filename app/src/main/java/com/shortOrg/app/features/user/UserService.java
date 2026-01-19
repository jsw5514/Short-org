package com.shortOrg.app.features.user;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.shared.dto.SignupRequest;
import com.shortOrg.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void userInsert(SignupRequest signupRequest) {
        User user = new User();
        user.setId(signupRequest.getId());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setAvgRate(null);
        user.setOrgTime(0L);

        userRepository.save(user);
    }

    public boolean idCheck(String id) {
        return userRepository.existsById(id);
    }

    public User userProfile(String id) {
        Optional<User> oUser = userRepository.findById(id);
        User user = null;
        if (oUser.isPresent()) {
            user = oUser.get();
        }
        return user;
    }
}
