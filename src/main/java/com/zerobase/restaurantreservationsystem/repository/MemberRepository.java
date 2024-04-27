package com.zerobase.restaurantreservationsystem.repository;

import com.zerobase.restaurantreservationsystem.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndRole(String email, String role);

    boolean existsByEmailAndRole(String email, String role);
}
