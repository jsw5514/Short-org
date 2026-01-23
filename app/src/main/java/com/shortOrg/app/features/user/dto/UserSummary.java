package com.shortOrg.app.features.user.dto;

import com.shortOrg.app.domain.User;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSummary {
    private String id;
    private String nickname;
    private String profileImage;

    public UserSummary(User user){
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.id = user.getId();
    }
}
