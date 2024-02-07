package de.allcom.controllers;

import de.allcom.dto.bet.NewBetDto;
import de.allcom.services.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuctionController {
    private final AuctionService auctionService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/makeBet")
    public void processBet(@Payload NewBetDto newBet, SimpMessageHeaderAccessor headerAccessor) {
        auctionService.addBet(newBet, headerAccessor);
    }
}
