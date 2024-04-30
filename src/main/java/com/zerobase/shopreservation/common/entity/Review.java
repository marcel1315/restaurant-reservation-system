package com.zerobase.shopreservation.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int rate;
    String contents;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    Reservation reservation;
}
