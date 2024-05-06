package com.zerobase.shopreservation.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int rate;
    String contents;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    Shop shop;
}
