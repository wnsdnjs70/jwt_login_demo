package com.example.demo.service.dto;

import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    private String account;   // 로그인 할 때 쓰는 아이디
    private String password;
    private String name;
    private String email;

    public static UserRegisterDTO of(String account, String password, String email, String name){
        return new UserRegisterDTO(account, password, email, name);
    }
}
