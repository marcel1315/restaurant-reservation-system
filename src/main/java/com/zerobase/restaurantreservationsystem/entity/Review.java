package com.zerobase.restaurantreservationsystem.entity;

import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int rate;
    String contents;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    Restaurant restaurant;
}
