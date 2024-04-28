package com.zerobase.restaurantreservationsystem.manager.service;

import com.zerobase.restaurantreservationsystem.common.entity.Member;
import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import com.zerobase.restaurantreservationsystem.common.exception.MemberNotExistException;
import com.zerobase.restaurantreservationsystem.common.exception.RestaurantNotExistException;
import com.zerobase.restaurantreservationsystem.common.repository.MemberRepository;
import com.zerobase.restaurantreservationsystem.common.type.MemberRole;
import com.zerobase.restaurantreservationsystem.manager.dto.ReservationApprovalDto;
import com.zerobase.restaurantreservationsystem.manager.dto.ReservationDto;
import com.zerobase.restaurantreservationsystem.manager.exception.ReservationNotExistException;
import com.zerobase.restaurantreservationsystem.manager.exception.RestaurantManagerNotMatchException;
import com.zerobase.restaurantreservationsystem.manager.repository.ReservationRepository;
import com.zerobase.restaurantreservationsystem.manager.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public List<ReservationDto> list(long restaurantId) {
        checkRestaurantManagerMatch(restaurantId);

        List<Reservation> reservations = reservationRepository.findAllByRestaurantId(restaurantId);
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (Reservation r : reservations) {
            reservationDtos.add(ReservationDto.of(r));
        }
        return reservationDtos;
    }

    public void updateApproval(ReservationApprovalDto approval) {
        checkRestaurantManagerMatch(approval.getRestaurantId());

        Optional<Reservation> optionalReservation = reservationRepository.findById(approval.getReservationId());
        if (!optionalReservation.isPresent()) {
            throw new ReservationNotExistException();
        }

        Reservation reservation = optionalReservation.get();
        reservation.setApprovalState(approval.getApprovalState());
        reservationRepository.save(reservation);
    }

    private void checkRestaurantManagerMatch(long restaurantId) {
        Member manager = getManager();
        List<Restaurant> restaurants = restaurantRepository.findByManager(manager);
        boolean found = false;
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId() == restaurantId) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RestaurantManagerNotMatchException();
        }
    }

    private Member getManager() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
            break;
        }

        Optional<Member> manager = memberRepository.findByEmailAndRole(username, MemberRole.valueOf(role));
        if (!manager.isPresent()) {
            throw new MemberNotExistException();
        }
        return manager.get();
    }
}
