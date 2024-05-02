package com.zerobase.shopreservation.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
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

    // 에약 가능 시간
    LocalTime reservationStartTimeWeekend;
    LocalTime reservationFinalTimeWeekend;
    LocalTime reservationStartTimeWeekday;
    LocalTime reservationFinalTimeWeekday;
    int reservationIntervalMinute;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Reservation> reservations;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Member manager;

    boolean deleteMarker;
}
