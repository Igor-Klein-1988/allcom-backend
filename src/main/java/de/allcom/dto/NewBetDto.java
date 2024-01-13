package de.allcom.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class NewBetDto {
    private Long auctionId;

    private Long userId;

    private Integer betAmount;
}
