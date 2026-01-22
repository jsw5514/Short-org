package com.shortOrg.app.domain;

import com.shortOrg.app.shared.dto.JoinMode;
import com.shortOrg.app.shared.dto.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "post")
public class Post {
    private static final int SRID_WGS84 = 4326;
    private static final GeometryFactory GF =
            new GeometryFactory(new PrecisionModel(), SRID_WGS84);

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Setter
    private String category;
    @Setter
    private String title;
    @Setter
    private String content;

    @Setter
    @Enumerated(EnumType.STRING)
    private PostStatus state; //모임 및 게시글 모집 상태

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "join_mode")
    private JoinMode joinMode;

    @Setter
    @Column(name = "last_modified")
    private LocalDateTime lastModified = LocalDateTime.now();

    @Setter
    @Column(name = "date_time")
    private LocalDateTime meetingTime;

    //모임용
    @Setter
    @Column(name = "location_name")
    private String locationName;
    
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(name = "location", columnDefinition = "POINT SRID 4326", nullable = false)
    private Point location;
    public void setLocationLngLat(double longitude, double latitude) {
        location = GF.createPoint(new Coordinate(longitude, latitude));
        location.setSRID(SRID_WGS84);
    }
    public double getLongitude() {
        return location.getCoordinate().getX();
    }
    public double getLatitude() {
        return location.getCoordinate().getY();
    }

    @Setter
    private Integer capacity;
    //TODO 이와 동등한 인덱스나 뷰 추가 필요
//    @Column(name = "capacity_joined")
//    private Integer capacityJoined; 

    @Builder
    public Post(
            User writer, 
            String category, 
            String title, 
            String content, 
            PostStatus state, 
            JoinMode joinMode, 
            LocalDateTime lastModified, 
            LocalDateTime meetingTime, 
            String locationName, 
            double longitude,
            double latitude,
            Integer capacity
    ) {
        this.writer = writer;
        this.category = category;
        this.title = title;
        this.content = content;
        this.state = state;
        this.joinMode = joinMode;
        this.lastModified = lastModified;
        this.meetingTime = meetingTime;
        this.locationName = locationName;
        this.capacity = capacity;
        this.setLocationLngLat(longitude,latitude);
    }
}