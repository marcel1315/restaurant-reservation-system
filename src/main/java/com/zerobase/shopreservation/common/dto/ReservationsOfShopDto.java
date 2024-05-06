package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.type.ReservationSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ReservationsOfShopDto {
    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    private long shopId;

    // sorting
    @NotNull
    @Schema(example = "RECENT", requiredMode = REQUIRED, allowableValues = {"RECENT"})
    private ReservationSort sortBy;

    // search
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "2024-05-06", defaultValue = "2024-05-06", requiredMode = NOT_REQUIRED)
    private LocalDate date;

    @Schema(example = "010-2222-3333", requiredMode = NOT_REQUIRED)
    private String phone;

    @Schema(example = "1", requiredMode = NOT_REQUIRED)
    private String customerId;

    // pagination
    @Positive
    @Schema(example = "10", requiredMode = REQUIRED)
    private int pageSize;

    @Positive
    @Schema(example = "1", requiredMode = REQUIRED)
    private int pageIndex;

    @Schema(hidden = true)
    public int getPageStart() {
        return (pageIndex - 1) * pageSize;
    }
}
