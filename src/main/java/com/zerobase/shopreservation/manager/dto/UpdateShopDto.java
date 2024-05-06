package com.zerobase.shopreservation.manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

@Data
@Builder
public class UpdateShopDto {
    @Schema(example = "진진칼국수")
    public String name;

    @Schema(example = "손칼국수 깔끔합니다.")
    public String description;

    @Schema(example = "37.42333241", requiredMode = NOT_REQUIRED)
    public String latitude;

    @Schema(example = "127.21223431", requiredMode = NOT_REQUIRED)
    public String longitude;

    @Schema(example = "서울특별시 강남구 강남대로 123")
    public String address;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "13:00", defaultValue = "13:00")
    public LocalTime reservationStartTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "22:00", defaultValue = "22:00")
    public LocalTime reservationFinalTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "11:00", defaultValue = "11:00")
    public LocalTime reservationStartTimeWeekend;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "23:00", defaultValue = "23:00")
    public LocalTime reservationFinalTimeWeekend;

    @Schema(example = "30")
    public Integer reservationIntervalMinute;
}
