package com.shortOrg.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(name="avg_rate")
    private Double avgRate;

    @Column(name="org_time")
    private Long orgTime;
}
