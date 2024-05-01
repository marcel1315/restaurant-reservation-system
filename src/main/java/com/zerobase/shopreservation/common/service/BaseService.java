package com.zerobase.shopreservation.common.service;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.exception.MemberNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;

public class BaseService {

    private MemberRepository memberRepository;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getCustomer() {
        Member m = getMember();
        if (m.getRole() != MemberRole.ROLE_CUSTOMER) {
            throw new MemberNotExistException();
        }
        return m;
    }

    public Member getManager() {
        Member m = getMember();
        if (m.getRole() != MemberRole.ROLE_MANAGER) {
            throw new MemberNotExistException();
        }
        return m;
    }

    private Member getMember() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
            break;
        }

        Optional<Member> manager = memberRepository.findByEmailAndRole(auth.getName(), MemberRole.valueOf(role));
        if (manager.isEmpty()) {
            throw new MemberNotExistException();
        }
        return manager.get();
    }
}
