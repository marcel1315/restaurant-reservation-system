package com.zerobase.shopreservation.manager.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
@Builder
public class UpdateShopDto {

    public String name;

    public String description;

    public String latitude;

    public String longitude;

    public String address;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationStartTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationFinalTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationStartTimeWeekend;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    public LocalTime reservationFinalTimeWeekend;

    public Integer reservationIntervalMinute;
}
