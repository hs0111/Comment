package com.example.practice.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
}
