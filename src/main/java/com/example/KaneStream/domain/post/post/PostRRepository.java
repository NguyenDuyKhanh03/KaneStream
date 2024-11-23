package com.example.KaneStream.domain.post.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRRepository extends JpaRepository<Post, UUID> {
}
