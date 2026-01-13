package com.shortOrg.app.features.user;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.user.dto.SignupRequest;
import com.shortOrg.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    // void와 차이점이 뭐야
    public void userInsert(SignupRequest signupRequest) {
        User user = new User();
        user.setId(signupRequest.getId());
        user.setPassword(signupRequest.getPassword());
        user.setAvgRate(null);
        user.setOrgTime(0L);
        userRepository.save(user);
    }

    public boolean idCheck(String id) {
        return userRepository.existsById(id);
    }
}
