package com.zerobase.shopreservation.common.entity;

import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int rate;
    String contents;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    Shop shop;
}
