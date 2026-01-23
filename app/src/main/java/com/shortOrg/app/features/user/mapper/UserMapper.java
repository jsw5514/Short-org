package com.shortOrg.app.features.user.mapper;

import com.shortOrg.app.domain.User;
import com.shortOrg.app.features.user.dto.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserSummary fromEntity(User user){
        return UserSummary.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}
