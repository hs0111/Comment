package com.example.practice.controller;

import com.example.practice.dto.CommentRequestDTO;
import com.example.practice.dto.CommentResponseDTO;
import com.example.practice.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDTO dto) {
        try {
            CommentResponseDTO response = commentService.createComment(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시글에 달린 댓글 전체 조회 (대댓글 포함)
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
        try {
            List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
