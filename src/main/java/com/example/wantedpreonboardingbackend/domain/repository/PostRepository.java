package com.example.wantedpreonboardingbackend.domain.repository;

import com.example.wantedpreonboardingbackend.domain.entity.Post;
import com.example.wantedpreonboardingbackend.dto.post.response.PostListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p left join fetch p.user where p.id = :id")
    Optional<Post> findPostById(Long id);

    Page<Post> findAllByOrderByIdAsc(Pageable pageable);

    @Query("select p from Post p left join fetch p.user where p.id = :id and p.user.id = :userId")
    Optional<Post> findUserPost(Long id, Long userId);
}
