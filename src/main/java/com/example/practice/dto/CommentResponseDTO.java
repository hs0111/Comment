package com.example.practice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {

    private Long id;
    private String content;

    private Long userId;
    private String username;  // 사용자 이름 (응답에 함께 표시)

    private Long postId;
    private Long parentId;

    private LocalDateTime createdAt;

    // 대댓글 포함 (재귀 구조)
    @Builder.Default
    private List<CommentResponseDTO> children = List.of();
}
