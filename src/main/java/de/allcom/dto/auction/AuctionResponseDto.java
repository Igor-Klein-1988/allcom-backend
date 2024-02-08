package de.allcom.dto.auction;

import de.allcom.models.Auction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "Auction Response Dto", description = "Auction data")
public class AuctionResponseDto {
    @Schema(description = "Auction identifier", example = "1")
    private Long id;

    @Schema(description = "The current scheduled date and time of the auction completion",
            example = "2023-12-01T15:30:45")
    private LocalDateTime currentPlannedEndAt;

    @Schema(description = "State of the auction", example = "ACTIVE")
    private Auction.State state;

    @Schema(description = "Product identifier", example = "2")
    private Long productId;

    @Schema(description = "LastBetAmount", example = "240")
    private Integer lastBetAmount;

    @Schema(description = "User's bet", example = "240")
    private Integer userLastBetAmount;

    public static AuctionResponseDto from(Auction auction, Integer lastBetAmount) {
        return AuctionResponseDto.builder()
                                 .id(auction.getId())
                                 .currentPlannedEndAt(auction.getCurrentPlannedEndAt())
                                 .state(auction.getState())
                                 .productId(auction.getProduct().getId())
                                 .lastBetAmount(lastBetAmount)
                                 .build();
    }
}
