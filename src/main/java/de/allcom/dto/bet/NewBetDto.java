package de.allcom.dto.bet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@Schema(name = "Bet", description = "Bet data")
public class NewBetDto {
    @Schema(description = "Auction identifier", example = "1")
    private Long auctionId;
    @Schema(description = "User identifier", example = "2")
    private Long userId;
    @Schema(description = "betAmount", example = "300")
    private Integer betAmount;
}
