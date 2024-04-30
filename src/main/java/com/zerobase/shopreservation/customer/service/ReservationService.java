package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.customer.dto.CheckInDto;
import com.zerobase.shopreservation.customer.dto.ReservationInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationOutputDto;
import com.zerobase.shopreservation.customer.repository.ReservationRepository;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.MemberNotExistException;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.manager.exception.ReservationNotExistException;
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
    private final ShopRepository shopRepository;

    public void reserve(ReservationInputDto reservationInputDto) {
        Reservation reservation = reservationInputDto.toReservation();
        reservation.setShop(getShop(reservationInputDto.getShopId()));
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

    private Shop getShop(long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        if (shop.isEmpty()) {
            throw new ShopNotExistException();
        }
        return shop.get();
    }

    public long checkIn(CheckInDto checkInDto) {
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndMember(
                checkInDto.getReservationId(),
                getCustomer()
        );

        if (optionalReservation.isEmpty()) {
            throw new ReservationNotExistException();
        }

        optionalReservation.get().setCheckInAt(checkInDto.getCheckInTime());
        Reservation reservation = reservationRepository.save(optionalReservation.get());

        return reservation.getId();
    }

    private Member getCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
            break;
        }

        Optional<Member> manager = memberRepository.findByEmailAndRole(username, MemberRole.valueOf(role));
        if (manager.isEmpty()) {
            throw new MemberNotExistException();
        }
        return manager.get();
    }
}
