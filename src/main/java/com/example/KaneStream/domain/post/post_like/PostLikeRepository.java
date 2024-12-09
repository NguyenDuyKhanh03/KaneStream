package com.example.KaneStream.domain.post.post_like;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    @NotNull Optional<PostLike> findById(@NotNull PostLikeId id);
}
