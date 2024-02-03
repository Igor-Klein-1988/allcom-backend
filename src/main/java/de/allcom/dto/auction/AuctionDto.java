package de.allcom.dto.auction;

import de.allcom.models.Auction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "Auction Dto", description = "Auction data")
public class AuctionDto {
    @NotNull(message = "Auction identifier is required")
    @Schema(description = "Auction identifier", example = "1")
    private Long id;

    @NotNull(message = "startPrice is required")
    @Schema(description = "startPrice", example = "100")
    private Integer startPrice;

    @NotNull(message = "startAt is required")
    @Schema(description = "Date and time of the auction start", example = "2023-12-01T15:30:45")
    private LocalDateTime startAt;

    @NotNull(message = "plannedEndAt is required")
    @Schema(description = "The originally scheduled date and time of the auction completion",
            example = "2023-12-01T15:30:45")
    private LocalDateTime plannedEndAt;

    @Schema(description = "The current scheduled date and time of the auction completion",
            example = "2023-12-01T15:30:45")
    private LocalDateTime currentPlannedEndAt;

    @Schema(description = "The actual date and time of the end of the auction", example = "2023-12-01T15:30:45")
    private LocalDateTime actualEndAt;

    @Schema(description = "State of the auction", example = "ACTIVE")
    private Auction.State state;

    @Schema(description = "Product identifier", example = "2")
    private Long productId;

    @Schema(description = "Winner identifier", example = "2")
    private Long winnerId;

    @Schema(description = "LastBetAmount", example = "240")
    private Integer lastBetAmount;

    @Schema(description = "The date and time when the object was updated", example = "2023-12-01T15:30:45")
    private LocalDateTime updatedAt;

    @Schema(description = "The date and time when the object was created", example = "2023-12-01T15:30:45")
    private LocalDateTime createdAt;

    public static AuctionDto from(Auction auction, Integer lastBetAmount) {
        Long winnerId = auction.getWinner() != null ? auction.getWinner().getId() : null;

        return AuctionDto.builder()
                         .id(auction.getId())
                         .startPrice(auction.getStartPrice())
                         .startAt(auction.getStartAt())
                         .plannedEndAt(auction.getPlannedEndAt())
                         .currentPlannedEndAt(auction.getCurrentPlannedEndAt())
                         .actualEndAt(auction.getActualEndAt())
                         .state(auction.getState())
                         .productId(auction.getProduct().getId())
                         .winnerId(winnerId)
                         .lastBetAmount(lastBetAmount)
                         .updatedAt(auction.getUpdatedAt())
                         .createdAt(auction.getCreatedAt())
                         .build();
    }
}
