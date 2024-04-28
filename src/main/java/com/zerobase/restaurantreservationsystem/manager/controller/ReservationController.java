package com.zerobase.restaurantreservationsystem.manager.controller;

import com.zerobase.restaurantreservationsystem.manager.dto.ReservationApprovalDto;
import com.zerobase.restaurantreservationsystem.manager.dto.ReservationDto;
import com.zerobase.restaurantreservationsystem.manager.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/manager/reservations")
    public ResponseEntity<?> list(@RequestParam Long restaurantId) {
        List<ReservationDto> list = reservationService.list(restaurantId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/manager/reservations")
    public ResponseEntity<?> updateApproval(@RequestBody ReservationApprovalDto approval) {
        reservationService.updateApproval(approval);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }
}
