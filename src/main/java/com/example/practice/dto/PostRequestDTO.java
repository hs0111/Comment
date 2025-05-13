package com.example.practice.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Setter x -> Builder 패턴으로 생성
public class PostRequestDTO {

    private String name;
    private String email;
    private String password;

    // 🔸 게시글을 등록할 사용자 ID를 같이 받는다 (PostEntity.user 연결용)
    private Long userId; // UserEntity 와 연결되어야 함 , PostEntity에 User를 매핑할 수 있음
}

// 클라이언트가 POST, PUT등의 요청으로 보낼 때 사용하는 DTO임
// 클라이언트 -> 서버 로 들어오는 데이터 구조!!