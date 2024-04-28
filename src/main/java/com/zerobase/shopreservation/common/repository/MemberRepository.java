package com.zerobase.shopreservation.common.repository;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.type.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndRole(String email, MemberRole role);

    boolean existsByEmailAndRole(String email, MemberRole role);
}
