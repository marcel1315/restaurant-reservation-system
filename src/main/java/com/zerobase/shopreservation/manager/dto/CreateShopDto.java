package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class CreateShopDto {
    @NotBlank
    @Schema(example = "진진칼국수")
    public String name;

    @NotBlank
    @Schema(example = "손칼국수 맛있습니다.")
    public String description;

    @NotBlank
    @Schema(example = "37.42333241", requiredMode = NOT_REQUIRED)
    public String latitude;

    @NotBlank
    @Schema(example = "127.21223431", requiredMode = NOT_REQUIRED)
    public String longitude;

    @NotBlank
    @Schema(example = "서울특별시 강남구 강남대로 123")
    public String address;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "13:00", defaultValue = "13:00", requiredMode = REQUIRED)
    public LocalTime reservationStartTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "22:00", defaultValue = "22:00", requiredMode = REQUIRED)
    public LocalTime reservationFinalTimeWeekday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "11:00", defaultValue = "11:00", requiredMode = REQUIRED)
    public LocalTime reservationStartTimeWeekend;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @Schema(example = "23:00", defaultValue = "23:00", requiredMode = REQUIRED)
    public LocalTime reservationFinalTimeWeekend;

    @Positive
    @Schema(example = "30", requiredMode = REQUIRED)
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
