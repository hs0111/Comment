package com.example.practice.service;

import com.example.practice.dto.UserRequestDTO;
import com.example.practice.dto.UserResponseDTO;
import com.example.practice.entity.UserEntity;
import com.example.practice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) throws Exception {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new Exception("이미 등록된 이메일입니다.");
        }

        UserEntity user = UserEntity.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        return toResponseDTO(userRepository.save(user));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) throws Exception {
        return userRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다."));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) throws Exception {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("사용자가 존재하지 않습니다."));

        // Entity는 setter가 없으므로 새 객체로 대체
        UserEntity updated = UserEntity.builder()
                .id(user.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .posts(user.getPosts())
                .build();

        return toResponseDTO(userRepository.save(updated));
    }

    public void deleteUser(Long id) throws Exception {
        if (!userRepository.existsById(id)) {
            throw new Exception("삭제할 사용자가 존재하지 않습니다.");
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO toResponseDTO(UserEntity entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .build();
    }
}
