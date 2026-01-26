package com.shortOrg.app.domain;

import com.shortOrg.app.shared.enumerate.ApplicantStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
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
    @Column(length = 20) // 길이가 작대요
    private ApplicantStatus state;
    
    @Builder
    public Applicant(Post post, User user, ApplicantStatus state) {
        this.post = post;
        this.user = user;
        this.state = state;
    }
}
