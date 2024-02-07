package de.allcom.controllers.api;

import de.allcom.dto.bet.NewBetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;


@Tags(@Tag(name = "Auctions"))
public interface AuctionApi {
    @Operation(summary = "Process a new bet on an auction",
            description = "Available for checked and not blocked Clients.")
    @MessageMapping("/makeBet")
    void processBet(@Payload NewBetDto newBet, SimpMessageHeaderAccessor headerAccessor);
}