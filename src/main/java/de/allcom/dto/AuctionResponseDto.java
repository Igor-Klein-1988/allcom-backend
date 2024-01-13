package de.allcom.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuctionResponseDto {
    private Long id;
    public String number;
    private Long productId;
    private Integer lastBetAmount;
    private boolean isFinished;
}
