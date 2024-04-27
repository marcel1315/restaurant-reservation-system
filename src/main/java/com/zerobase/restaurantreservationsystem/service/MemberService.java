package com.zerobase.restaurantreservationsystem.service;

import com.zerobase.restaurantreservationsystem.dto.LoginDto;
import com.zerobase.restaurantreservationsystem.dto.SignupDto;
import com.zerobase.restaurantreservationsystem.entity.Member;
import com.zerobase.restaurantreservationsystem.exception.IncorrectPasswordException;
import com.zerobase.restaurantreservationsystem.exception.InvalidUsernameAndRoleFormat;
import com.zerobase.restaurantreservationsystem.exception.MemberAlreadyExistException;
import com.zerobase.restaurantreservationsystem.exception.MemberNotExistException;
import com.zerobase.restaurantreservationsystem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public boolean signup(SignupDto signupDto) {
        if (memberRepository.existsByEmailAndRole(
                signupDto.getEmail(),
                signupDto.getRole()
        )) {
            throw new MemberAlreadyExistException();
        }

        Member member = signupDto.toMember();
        memberRepository.save(member);

        return true;
    }

    public boolean login(LoginDto loginDto) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndRole(
                loginDto.getUsername(),
                loginDto.getRole()
        );
        if (!optionalMember.isPresent()) {
            throw new MemberNotExistException();
        }
        Member member = optionalMember.get();

        if (!member.getPassword().equals(loginDto.getPassword())) {
            throw new IncorrectPasswordException();
        }

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameAndRole) throws UsernameNotFoundException {
        String[] contents = usernameAndRole.split(" ");
        if (contents.length != 2) {
            throw new InvalidUsernameAndRoleFormat();
        }
        String username = contents[0];
        String role = contents[1];

        Optional<Member> member = memberRepository.findByEmailAndRole(username, role);

        if (!member.isPresent()) {
            throw new MemberNotExistException();
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(member.get().getRole()));

        return new User(username, member.get().getPassword(), grantedAuthorities);
    }
}
