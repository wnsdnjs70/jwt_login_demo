package com.example.demo.controller;


import com.example.demo.controller.request.AuthenticationRequest;
import com.example.demo.controller.request.UserRegisterRequest;
import com.example.demo.controller.response.AuthenticationResponse;
import com.example.demo.domain.Users.Users;
import com.example.demo.domain.Users.UsersRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.UsersDetailService;
import com.example.demo.utility.JwtUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class UserController {

    private final UserService userService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UsersDetailService usersDetailService;

    @PostMapping("/register")
    @ApiOperation(value = "회원가입")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserRegisterRequest request) {
        Map<String, Object> result = this.userService.create(request.toServiceDto());

        if ((Boolean) result.get("success")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인 API")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        String account = authenticationRequest.getAccount();
        Users foundUser = userService.findUser(account);


        if (!passwordEncoder.matches(authenticationRequest.getPassword(), foundUser.getPassword())) {
            return ResponseEntity.ok("Password invalid");
        }

        final UserDetails userDetails = usersDetailService.loadUserByUsername(account);
        final String token = jwtUtils.createToken(userDetails.getUsername(), foundUser.getRoles());
        final Users user = usersDetailService.getUsers(account);

        return ResponseEntity.ok(new AuthenticationResponse(token, user));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그 아웃 API")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 인증된 사용자의 인증 토큰 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 토큰이 존재하면 로그아웃 처리
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return ResponseEntity.ok("로그 아웃 되었습니다.");
    }

}
