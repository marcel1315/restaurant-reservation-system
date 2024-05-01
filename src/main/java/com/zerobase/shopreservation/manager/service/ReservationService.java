package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.MemberNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.manager.dto.ReservationApprovalDto;
import com.zerobase.shopreservation.manager.dto.ReservationDto;
import com.zerobase.shopreservation.manager.exception.ReservationNotExistException;
import com.zerobase.shopreservation.manager.exception.ShopManagerNotMatchException;
import com.zerobase.shopreservation.manager.repository.ReservationRepository;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
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
public class ReservationService extends BaseService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;

    public List<ReservationDto> list(long shopId) {
        checkShopManagerMatch(shopId);

        List<Reservation> reservations = reservationRepository.findAllByShopId(shopId);
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (Reservation r : reservations) {
            reservationDtos.add(ReservationDto.of(r));
        }
        return reservationDtos;
    }

    public void updateApproval(ReservationApprovalDto approval) {
        checkShopManagerMatch(approval.getShopId());

        Optional<Reservation> optionalReservation = reservationRepository.findById(approval.getReservationId());
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotExistException();
        }

        Reservation reservation = optionalReservation.get();
        reservation.setApprovalState(approval.getApprovalState());
        reservationRepository.save(reservation);
    }

    private void checkShopManagerMatch(long shopId) {
        Member manager = getManager();
        List<Shop> shops = shopRepository.findByManagerAndDeleteMarker(manager, false);
        boolean found = false;
        for (Shop shop : shops) {
            if (shop.getId() == shopId) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ShopManagerNotMatchException();
        }
    }
}
