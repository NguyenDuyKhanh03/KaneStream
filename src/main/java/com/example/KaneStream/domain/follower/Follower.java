package com.example.KaneStream.domain.follower;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "followers")
public class Follower {

    @EmbeddedId
    private FollowerId id;

//    @MapsId("followerId")
//    @Column(name = "follower_id")
//    private UUID followerId;
//
//    @MapsId("followingId")
//    @Column(name = "following_id")
//    private UUID followingId;

    @Column(name = "created_at")
    @CreatedDate
    private Timestamp createdAt;
}
