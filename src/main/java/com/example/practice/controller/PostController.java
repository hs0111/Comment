package com.example.practice.controller;

import com.example.practice.dto.PostRequestDTO;
import com.example.practice.dto.PostResponseDTO;
import com.example.practice.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequestDTO requestDTO) {
        try {
            PostResponseDTO response = postService.createPost(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostResponseDTO response = postService.getPostById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO requestDTO) {
        try {
            PostResponseDTO response = postService.updatePost(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("게시글이 정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
