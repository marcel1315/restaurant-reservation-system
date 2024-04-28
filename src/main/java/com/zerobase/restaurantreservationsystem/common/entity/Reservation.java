package com.zerobase.restaurantreservationsystem.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    LocalDateTime schedule;
    String phone;
    String approval;
    boolean checkedIn;
    int personCount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    Restaurant restaurant;
}
