package de.allcom.dto.auction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "Auction Request", description = "Auction Request's data")
public class AuctionRequestDto {
    @NotNull(message = "startPrice is required")
    @Schema(description = "StartPrice", example = "200")
    private Integer startPrice;

    @NotNull(message = "startAt is required")
    @Schema(description = "StartAt", example = "2024-01-16T11:15:30")
    private LocalDateTime startAt;

    @NotNull(message = "plannedEndAt is required")
    @Schema(description = "PlannedEndAt", example = "2024-01-16T11:15:30")
    private LocalDateTime plannedEndAt;
}
