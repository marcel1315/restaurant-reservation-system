package com.zerobase.restaurantreservationsystem.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.List;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String description;
    String address;
    String latitude;
    String longitude;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Reservation> reservations;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Member manager;
}
