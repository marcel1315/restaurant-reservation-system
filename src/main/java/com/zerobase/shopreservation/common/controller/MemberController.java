package com.zerobase.shopreservation.common.controller;

import com.zerobase.shopreservation.common.dto.LoginDto;
import com.zerobase.shopreservation.common.dto.MemberOutputDto;
import com.zerobase.shopreservation.common.dto.SignupDto;
import com.zerobase.shopreservation.common.security.TokenProvider;
import com.zerobase.shopreservation.common.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "1 - Member", description = "회원 관련")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @Operation(
            summary = "회원가입"
    )
    @PostMapping("/member/signup")
    public ResponseEntity<?> signup(@Validated @RequestBody SignupDto signupDto) {
        memberService.signup(signupDto);
        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "로그인",
            description = "로그인 후 jwt 토큰을 받게 됩니다.\n" +
                    "해당 토큰을 상단의 'Authorize' 버튼을 눌러 입력해주세요."
    )
    @PostMapping("/member/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDto loginDto) {
        memberService.login(loginDto);
        String token = tokenProvider.generateToken(loginDto.getEmail(), loginDto.getRoleString());
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("token", token);
        return ResponseEntity.ok(resultMap);
    }

    @Operation(
            summary = "회원정보 보기"
    )
    @GetMapping("/member/info")
    public ResponseEntity<?> info() {
        MemberOutputDto memberOutputDto = memberService.info();
        return ResponseEntity.ok(memberOutputDto);
    }
}