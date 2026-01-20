package com.shortOrg.app.domain;

import com.shortOrg.app.shared.dto.ApplicantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(
        name = "applicant",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_applicant_post_user",
                columnNames = {"post_id", "user_id"}
        )
)
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ApplicantStatus state;
}
