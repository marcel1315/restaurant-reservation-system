package com.zerobase.restaurantreservationsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Member {
    @Id
    long id;
    String email;
    String username;
    String password;
    String phone;
    String role;
}
