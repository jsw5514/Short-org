package com.shortOrg.app.repository.projection;

import com.shortOrg.app.shared.enumerate.JoinMode;
import com.shortOrg.app.shared.enumerate.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDistanceView {
    //Post 테이블 컬럼들----------------------------------------
    private Long id;
    private String writerId;
    private String writerNickname;
    private String writerProfileImage;
    private String category;
    private String title;
    private String content;
    private PostStatus state;
    private JoinMode joinMode;
    private LocalDateTime lastModified;
    private LocalDateTime meetingTime;
    private String locationName;
    private double longitude;
    private double latitude;
    private int capacity;
    
    //Post 테이블에는 없는 뷰 용 컬럼들
    private Integer slack;
    private Double distance;
}
