package de.allcom.dto;

import de.allcom.models.Auction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionRequestDto {
    private Long id;
    @Schema(description = "number", example = "AO2024SS")
    public String number;
    @Schema(description = "productId", example = "2")
    private Long productId;
    @Schema(description = "startPrice", example = "200")
    private Integer startPrice;

    @Schema(description = "startAt", example = "2024-01-07T18:15:30")
    private LocalDateTime startAt;

    @Schema(description = "plannedEndAt", example = "2024-01-07T20:15:30")
    private LocalDateTime plannedEndAt;
}
