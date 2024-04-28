package com.zerobase.restaurantreservationsystem.common.repository;

import com.zerobase.restaurantreservationsystem.common.entity.Member;
import com.zerobase.restaurantreservationsystem.common.type.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndRole(String email, MemberRole role);

    boolean existsByEmailAndRole(String email, MemberRole role);
}
