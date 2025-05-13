package com.example.practice.service;

import com.example.practice.dto.CommentRequestDTO;
import com.example.practice.dto.CommentResponseDTO;
import com.example.practice.entity.CommentEntity;
import com.example.practice.entity.PostEntity;
import com.example.practice.entity.UserEntity;
import com.example.practice.repository.CommentRepository;
import com.example.practice.repository.PostRepository;
import com.example.practice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 댓글 등록
    public CommentResponseDTO createComment(CommentRequestDTO dto) throws Exception {
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new Exception("댓글 내용이 비어 있습니다.");
        }

        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new Exception("작성자(User)가 존재하지 않습니다."));

        PostEntity post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));

        CommentEntity parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new Exception("부모 댓글이 존재하지 않습니다."));
        }

        CommentEntity comment = CommentEntity.builder()
                .content(dto.getContent())
                .user(user)
                .post(post)
                .parent(parent)
                .build();

        CommentEntity saved = commentRepository.save(comment);
        return toResponseDTO(saved);
    }

    // 게시글별 댓글 전체 조회 (대댓글 포함)
    public List<CommentResponseDTO> getCommentsByPost(Long postId) throws Exception {
        if (!postRepository.existsById(postId)) {
            throw new Exception("해당 게시글이 존재하지 않습니다.");
        }

        List<CommentEntity> comments = commentRepository.findByPostId(postId);

        // 최상위 댓글만 걸러냄
        List<CommentEntity> rootComments = comments.stream()
                .filter(c -> c.getParent() == null)
                .collect(Collectors.toList());

        return rootComments.stream()
                .map(this::toResponseDTOWithChildren)
                .collect(Collectors.toList());
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) throws Exception {
        if (!commentRepository.existsById(commentId)) {
            throw new Exception("삭제할 댓글이 존재하지 않습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    // 단일 댓글 응답 DTO 변환
    private CommentResponseDTO toResponseDTO(CommentEntity entity) {
        return CommentResponseDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .userId(entity.getUser().getId())
                .username(entity.getUser().getUsername())
                .postId(entity.getPost().getId())
                .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    // 자식 댓글까지 포함하는 재귀 DTO 생성
    private CommentResponseDTO toResponseDTOWithChildren(CommentEntity entity) {
        return CommentResponseDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .userId(entity.getUser().getId())
                .username(entity.getUser().getUsername())
                .postId(entity.getPost().getId())
                .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
                .createdAt(entity.getCreatedAt())
                .children(entity.getChildren().stream()
                        .map(this::toResponseDTOWithChildren)
                        .collect(Collectors.toList()))
                .build();
    }
}
