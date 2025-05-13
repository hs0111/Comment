package com.example.practice.repository;

import com.example.practice.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // 게시글 ID로 댓글 목록 조회 (최상위 댓글들)
    List<CommentEntity> findByPostId(Long postId);

    // 부모 댓글 ID로 대댓글 조회
    List<CommentEntity> findByParentId(Long parentId);
}
