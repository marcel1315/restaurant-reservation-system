package com.zerobase.restaurantreservationsystem.customer.service;

import com.zerobase.restaurantreservationsystem.customer.dto.ReservationInputDto;
import com.zerobase.restaurantreservationsystem.customer.dto.ReservationOutputDto;
import com.zerobase.restaurantreservationsystem.customer.repository.ReservationRepository;
import com.zerobase.restaurantreservationsystem.customer.repository.RestaurantRepository;
import com.zerobase.restaurantreservationsystem.common.entity.Member;
import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import com.zerobase.restaurantreservationsystem.common.exception.MemberNotExistException;
import com.zerobase.restaurantreservationsystem.common.exception.RestaurantNotExistException;
import com.zerobase.restaurantreservationsystem.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service("customerReservationService")
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    public void reserve(ReservationInputDto reservationInputDto) {
        Reservation reservation = reservationInputDto.toReservation();
        reservation.setRestaurant(getRestaurant(reservationInputDto.getRestaurantId()));
        reservation.setMember(getCustomer());

        reservationRepository.save(reservation);
    }

    public List<ReservationOutputDto> list() {
        List<Reservation> list = reservationRepository.findByMemberOrderByScheduleDesc(getCustomer());
        List<ReservationOutputDto> listDto = new ArrayList<>();
        for (Reservation r : list) {
            listDto.add(ReservationOutputDto.of(r));
        }
        return listDto;
    }

    private Restaurant getRestaurant(long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (!restaurant.isPresent()) {
            throw new RestaurantNotExistException();
        }
        return restaurant.get();
    }

    private Member getCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
            break;
        }

        Optional<Member> manager = memberRepository.findByEmailAndRole(username, role);
        if (!manager.isPresent()) {
            throw new MemberNotExistException();
        }
        return manager.get();
    }
}
