package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
@Builder
public class CreateShopDto {
    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotBlank
    public String latitude;

    @NotBlank
    public String longitude;

    @NotBlank
    public String address;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationStartTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationFinalTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationStartTimeWeekend;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationFinalTimeWeekend;

    @Positive
    public int reservationIntervalMinute;

    public Shop toShop() {
        return Shop.builder()
                .name(getName())
                .address(getAddress())
                .description(getDescription())
                .latitude(getLatitude())
                .longitude(getLongitude())
                .reservationStartTimeWeekday(reservationStartTimeWeekday)
                .reservationFinalTimeWeekday(reservationFinalTimeWeekday)
                .reservationStartTimeWeekend(reservationStartTimeWeekend)
                .reservationFinalTimeWeekend(reservationFinalTimeWeekend)
                .reservationIntervalMinute(reservationIntervalMinute)
                .build();
    }
}
