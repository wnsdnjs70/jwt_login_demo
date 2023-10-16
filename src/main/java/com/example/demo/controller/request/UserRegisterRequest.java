package com.example.demo.controller.request;

import com.example.demo.service.dto.UserRegisterDTO;
import com.sun.istack.NotNull;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @NotNull
    private String account;   // 로그인 할 때 쓰는 아이디

    @NotNull
    private String password;

    private String email;
    private String name;

    public UserRegisterDTO toServiceDto(){
        return UserRegisterDTO.of(account, password, email, name);
    }

}
