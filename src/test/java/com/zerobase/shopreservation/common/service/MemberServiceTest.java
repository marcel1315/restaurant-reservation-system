package com.zerobase.shopreservation.common.service;

import com.zerobase.shopreservation.common.dto.LoginDto;
import com.zerobase.shopreservation.common.dto.SignupDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.exception.IncorrectPasswordException;
import com.zerobase.shopreservation.common.exception.InvalidUsernameAndRoleFormat;
import com.zerobase.shopreservation.common.exception.MemberAlreadyExistException;
import com.zerobase.shopreservation.common.exception.MemberNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 - 성공")
    void signup() {
        //given
        SignupDto dto = SignupDto.builder()
                .phone("010-1234-5678")
                .name("홍길동")
                .email("abc@gmail.com")
                .password("1234")
                .build();

        //when
        memberService.signup(dto);

        //then
        verify(memberRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("회원가입 - 성공(패스워드 암호화 저장)")
    void signup_encrypt_password() {
        //given
        String password = "1234";
        String email = "abc@gmail.com";
        SignupDto dto = SignupDto.builder()
                .phone("010-1234-5678")
                .name("홍길동")
                .email(email)
                .password(password)
                .build();
        given(passwordEncoder.encode(password))
                .willReturn("somehashedvalue");

        //when
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        memberService.signup(dto);

        //then
        verify(memberRepository).save(captor.capture());
        assertNotNull(captor);
        assertNotEquals(password, captor.getValue().getPassword());
        assertEquals(email, captor.getValue().getEmail());
    }

    @Test
    @DisplayName("회원가입 - 실패(같은 이메일로 시도)")
    void signup_twice() {
        //given
        SignupDto dto = SignupDto.builder()
                .phone("010-1234-5678")
                .name("홍길동")
                .email("abc@gmail.com")
                .password("1234")
                .build();
        given(memberRepository.existsByEmailAndRole(any(), any()))
                .willReturn(true);

        //when
        //then
        assertThrows(MemberAlreadyExistException.class,
                () -> memberService.signup(dto)
        );
    }

    @Test
    @DisplayName("로그인 - 성공")
    void login() {
        //given
        LoginDto dto = LoginDto.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build();
        Member m = Member.builder().build();
        given(memberRepository.findByEmailAndRole(any(), any()))
                .willReturn(Optional.of(m));
        given(passwordEncoder.matches(any(), any()))
                .willReturn(true);

        //when
        //then
        assertDoesNotThrow(() -> memberService.login(dto));
    }

    @Test
    @DisplayName("로그인 - 실패(회원정보가 없음)")
    void login_fail_not_member() {
        //given
        LoginDto dto = LoginDto.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build();
        Member m = Member.builder().build();
        given(memberRepository.findByEmailAndRole(any(), any()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(MemberNotExistException.class,
                () -> memberService.login(dto)
        );
    }

    @Test
    @DisplayName("로그인 - 실패(비밀번호 틀림)")
    void login_fail_wrong_password() {
        //given
        LoginDto dto = LoginDto.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build();
        Member m = Member.builder()
                .password("1111")
                .build();
        given(memberRepository.findByEmailAndRole(any(), any()))
                .willReturn(Optional.of(m));
        given(passwordEncoder.matches(any(), any()))
                .willReturn(false);

        //when
        //then
        assertThrows(IncorrectPasswordException.class,
                () -> memberService.login(dto)
        );
    }

    @Test
    @DisplayName("토큰으로 사용자 검증 - 성공")
    void load_user_by_token() {
        //given
        String email = "abc@gmail.com";
        String password = "somehashedvalue";
        MemberRole role = MemberRole.ROLE_CUSTOMER;
        given(memberRepository.findByEmailAndRole(email, role))
                .willReturn(Optional.of(Member.builder()
                        .email(email)
                        .password(password)
                        .role(role)
                        .build())
                );

        //when
        UserDetails userDetails = memberService.loadUserByUsername(email + " " + role.toString());

        //then
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(role.toString(), userDetails.getAuthorities().stream().findFirst().get().toString());
    }

    @Test
    @DisplayName("토큰으로 사용자 검증 - 실패(인자의 포맷이 다를 때)")
    void load_user_by_token_fail_invalid_format() {
        //given
        String[] failCases = {
                "abc@email.com",
                "abc@email.comROLE_CUSTOMER", // 붙여쓰면 안됨
                "abc@email.com|ROLE_CUSTOMER", // 공백문자로 구분해야 함
                " abc@email.com ROLE_CUSTOMER", // 가장 앞에 공백이 있으면 안됨
                "abc@email.com ROLE_CUSTOM", // ROLE_CUSTOMER 문자여야 함. MemberRole 사용
                "abc@email.com CUSTOMER",
                "ROLE_CUSTOM",
        };
        String[] successCases = {
                "abc@email.com ROLE_CUSTOMER",
                "abc@email.com ROLE_MANAGER"
        };
        given(memberRepository.findByEmailAndRole(any(), any()))
                .willReturn(Optional.of(Member.builder()
                        .email("abc@gmail.com")
                        .password("somehashedvalue")
                        .role(MemberRole.ROLE_CUSTOMER) // 이 부분은 끝에서 User를 생성할 때 빈값이 들어가지 않도록 넣어준 부분
                        .build()));

        //when
        //then
        for (String f : failCases) {
            assertThrows(InvalidUsernameAndRoleFormat.class,
                    () -> memberService.loadUserByUsername(f)
            );
        }
        for (String s : successCases) {
            memberService.loadUserByUsername(s);
        }
    }

    @Test
    @DisplayName("토큰으로 사용자 검증 - 실패(사용자가 존재하지 않을 때)")
    void load_user_by_token_fail_member_doesnt_exist() {
        //given
        String usernameAndRole = "abc@email.com ROLE_CUSTOMER";
        given(memberRepository.findByEmailAndRole(any(), any()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThrows(MemberNotExistException.class,
                () -> memberService.loadUserByUsername(usernameAndRole)
        );
    }
}