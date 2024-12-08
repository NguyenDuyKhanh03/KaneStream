package com.example.KaneStream.domain.user.entity;

import com.example.KaneStream.domain.user.Role;
import com.example.KaneStream.domain.user.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String cover;

    private String avatar;

    @Column(unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    private String salt;

    private String bio;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "posts_count")
    private int postsCount;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @CreationTimestamp
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::users_role_enum")
    private Role role;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::users_status_enum")
    private UserStatus status;

}
