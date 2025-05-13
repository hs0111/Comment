package com.example.practice.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    private String content;

    private Long userId;   // 댓글 작성자
    private Long postId;   // 연결된 게시글 ID
    private Long parentId; // 대댓글일 경우 부모 댓글 ID (null이면 일반 댓글)
}
