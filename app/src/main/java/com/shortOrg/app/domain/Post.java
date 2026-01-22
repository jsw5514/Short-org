package com.shortOrg.app.domain;

import com.shortOrg.app.shared.dto.JoinMode;
import com.shortOrg.app.shared.dto.PostStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id")
    private User writerId;

    private String category;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus state; //모임 및 게시글 모집 상태

    @Enumerated(EnumType.STRING)
    @Column(name = "join_mode")
    private JoinMode joinMode;

    @Column(name = "last_modified")
    private LocalDateTime lastModified = LocalDateTime.now();

    @Column(name = "date_time")
    private LocalDateTime meetingTime;

    //모임용
    @Column(name = "location_name")
    private String locationName;
    
    private Double longitude;
    private Double latitude;
    
    private Integer capacity;
    //TODO 이와 동등한 인덱스나 뷰 추가 필요
//    @Column(name = "capacity_joined")
//    private Integer capacityJoined; 
}