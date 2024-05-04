package com.zerobase.shopreservation.common.service;

import com.zerobase.shopreservation.common.dto.LoginDto;
import com.zerobase.shopreservation.common.dto.MemberOutputDto;
import com.zerobase.shopreservation.common.dto.SignupDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.exception.IncorrectPasswordException;
import com.zerobase.shopreservation.common.exception.InvalidUsernameAndRoleFormat;
import com.zerobase.shopreservation.common.exception.MemberAlreadyExistException;
import com.zerobase.shopreservation.common.exception.MemberNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * signupDto.getRole을 통해 customer인지 manager인지 구분함
     */
    @Transactional
    public void signup(SignupDto signupDto) {
        if (memberRepository.existsByEmailAndRole(
                signupDto.getEmail(),
                signupDto.getRole()
        )) {
            throw new MemberAlreadyExistException();
        }

        Member member = signupDto.toMember();
        String encPassword = passwordEncoder.encode(signupDto.getPassword());
        member.setPassword(encPassword);

        memberRepository.save(member);
    }

    /**
     * 로그인
     * LoginDto.getRole을 통해 customer인지 manager인지 구분함
     */
    public void login(LoginDto loginDto) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndRole(
                loginDto.getEmail(),
                loginDto.getRole()
        );
        if (optionalMember.isEmpty()) {
            throw new MemberNotExistException();
        }
        Member member = optionalMember.get();
        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new IncorrectPasswordException();
        }
    }

    /**
     * 회면 정보 보기
     */
    public MemberOutputDto info() {
        return MemberOutputDto.of(getMember());
    }

    /**
     * 토큰으로 사용자를 확인할 때 사용됨
     * Spring security의 UserDetailsService의 메서드를 구현함
     * username과 role을 동시에 인자로 받아 특정 User(Member)를 인식함 -> 같은 이메일의 customer와 manager 이메일 생성이 가능함
     * username과 role을 공백문자로 합친 값을 usernameAndRole로 넣어줘야 함.
     * e.g., "abc@gmail.com ROLE_CUSTOMER"
     */
    @Override
    public UserDetails loadUserByUsername(String usernameAndRole) throws UsernameNotFoundException {
        String[] contents = usernameAndRole.split(" ");
        if (contents.length != 2) {
            throw new InvalidUsernameAndRoleFormat();
        }
        try {
            MemberRole.valueOf(contents[1]);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new InvalidUsernameAndRoleFormat();
        }
        String username = contents[0];
        String role = contents[1];
        Optional<Member> member = memberRepository.findByEmailAndRole(username, MemberRole.valueOf(role));

        if (member.isEmpty()) {
            throw new MemberNotExistException(username, role);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));

        return new User(username, member.get().getPassword(), grantedAuthorities);
    }

    private Member getMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
            break;
        }

        Optional<Member> member = memberRepository.findByEmailAndRole(auth.getName(), MemberRole.valueOf(role));
        if (member.isEmpty()) {
            throw new MemberNotExistException();
        }
        return member.get();
    }
}
