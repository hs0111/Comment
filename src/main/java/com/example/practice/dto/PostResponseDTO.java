package com.example.practice.dto;

import lombok.*;

// @Data 제거함 !!
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {

    private Long id;
    private String name;
    private String email;

    // 비밀번호는 응답에 포함하지 않음 (보안 고려)
}

// 서버가 클라이언트에게 반환할 때(GET, POST 응답) 사용하는 DTO임
// 서버 -> 클라이언트 로 나가는 데이터 구조
