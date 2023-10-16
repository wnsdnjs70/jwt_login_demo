package com.example.demo.service;

import com.example.demo.domain.Users.Users;
import com.example.demo.domain.Users.UsersRepository;
import com.example.demo.service.dto.UserRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> create(UserRegisterDTO dto) {

        Map<String, Object> resultMap = new HashMap<>();

        // 아이디가 중복되었을 때
        if (usersRepository.findByAccount(dto.getAccount()).isPresent()) {
            resultMap.put("success", false);
            resultMap.put("message", "중복된 아이디 입니다.");
            return resultMap;
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        Users newUser =
                Users.builder()
                    .account(dto.getAccount())
                    .password(encodedPassword)
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .roles(Collections.singletonList("ROLE_USER"))
                .build();

        try {
            usersRepository.save(newUser);
            resultMap.put("success", true);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
        }

        return resultMap;
    }

    public Users findUser(String account) {
        return usersRepository.findByAccount(account).orElseThrow();
    }
}
