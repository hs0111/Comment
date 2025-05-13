package com.example.practice.repository;

import com.example.practice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 이메일로 사용자 조회 (필요 시 중복 체크 또는 로그인용)
    Optional<UserEntity> findByEmail(String email);

    // 사용자명으로도 조회할 수 있도록 선택적으로 추가 가능
    Optional<UserEntity> findByUsername(String username);

    // 이메일 중복 여부 확인
    boolean existsByEmail(String email);
}
