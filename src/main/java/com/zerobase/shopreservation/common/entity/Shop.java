package com.zerobase.shopreservation.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String description;
    String address;
    String latitude;
    String longitude;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Reservation> reservations;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Member manager;

    boolean deleteMarker;
}
