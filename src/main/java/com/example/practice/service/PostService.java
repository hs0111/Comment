package com.example.practice.service;

import com.example.practice.dto.PostRequestDTO;
import com.example.practice.dto.PostResponseDTO;
import com.example.practice.entity.PostEntity;
import com.example.practice.entity.UserEntity;
import com.example.practice.repository.PostRepository;
import com.example.practice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Create
    public PostResponseDTO createPost(PostRequestDTO requestDTO) throws Exception {
        validateRequest(requestDTO);

        if (postRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        // 작성자 userId로 UserEntity 찾기
        UserEntity user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new Exception("작성자(User)를 찾을 수 없습니다."));

        // Builder로 Entity 생성
        PostEntity post = PostEntity.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .user(user)
                .build();

        PostEntity saved = postRepository.save(post);

        return toResponseDTO(saved);
    }

    // Read All
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Read by ID
    public PostResponseDTO getPostById(Long id) throws Exception {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 게시글을 찾을 수 없습니다."));
        return toResponseDTO(post);
    }

    // Update
    public PostResponseDTO updatePost(Long id, PostRequestDTO requestDTO) throws Exception {
        validateRequest(requestDTO);

        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 게시글이 존재하지 않습니다."));

        // Setter 대신 새 인스턴스 생성 (Immutable 방식)
        PostEntity updated = PostEntity.builder()
                .id(post.getId()) // 유지
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .user(post.getUser()) // 기존 작성자 유지
                .build();

        return toResponseDTO(postRepository.save(updated));
    }

    // Delete
    public void deletePost(Long id) throws Exception {
        if (!postRepository.existsById(id)) {
            throw new Exception("삭제할 게시글이 존재하지 않습니다.");
        }
        postRepository.deleteById(id);
    }

    // DTO 변환
    private PostResponseDTO toResponseDTO(PostEntity entity) {
        return PostResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    // 입력 검증 공통화
    private void validateRequest(PostRequestDTO dto) throws Exception {
        if (dto == null)
            throw new Exception("요청 데이터가 없습니다.");
        if (isBlank(dto.getName()))
            throw new Exception("이름이 비어 있습니다.");
        if (isBlank(dto.getEmail()))
            throw new Exception("이메일이 비어 있습니다.");
        if (isBlank(dto.getPassword()))
            throw new Exception("비밀번호가 비어 있습니다.");
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
