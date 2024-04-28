package com.zerobase.shopreservation.common.controller;

import com.zerobase.shopreservation.common.dto.LoginDto;
import com.zerobase.shopreservation.common.dto.SignupDto;
import com.zerobase.shopreservation.common.security.TokenProvider;
import com.zerobase.shopreservation.common.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/member/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupDto) {
        boolean success = memberService.signup(signupDto);
        HashMap<String, Boolean> resultMap = new HashMap<>();
        resultMap.put("success", success);
        return ResponseEntity.ok(resultMap);
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        boolean success = memberService.login(loginDto);
        if (success) {
            String token = tokenProvider.generateToken(loginDto.getUsername(), loginDto.getRoleString());
            HashMap<String, String> resultMap = new HashMap<>();
            resultMap.put("token", token);
            return ResponseEntity.ok(resultMap);
        }
        return ResponseEntity.badRequest().body("Login failed");
    }
}