package com.example.demo.controller.response;

import com.example.demo.domain.Users.Users;
import lombok.Getter;

@Getter
public class AuthenticationResponse {
    private final String jwt;
    private final Users users;

    public AuthenticationResponse(String jwt, Users users) {
        this.jwt = jwt;
        this.users = users;
    }
}
