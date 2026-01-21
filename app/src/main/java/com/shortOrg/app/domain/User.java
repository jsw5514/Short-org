package com.shortOrg.app.domain;

import com.shortOrg.app.shared.dto.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    private String id;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    @Column(name="avg_rate")
    private Double avgRate;

    @Column(name="org_time")
    private Long orgTime;

    private String nickname;

    @Column(name="profile_image")
    private String profileImage;
}




