package com.zerobase.restaurantreservationsystem.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    LocalDateTime schedule;
    String phone;
    boolean approved;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @ToString.Exclude
    Restaurant restaurant;
}
