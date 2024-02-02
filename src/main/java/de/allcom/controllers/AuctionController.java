package de.allcom.controllers;

import de.allcom.controllers.api.AuctionApi;
import de.allcom.dto.auction.AuctionResponseDto;
import de.allcom.dto.bet.NewBetDto;
import de.allcom.services.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuctionController implements AuctionApi {
    private  final AuctionService auctionService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @MessageMapping("/makeBet")
    public void processBet(@Payload NewBetDto newBet) {
        System.out.println("NewBetDto: " + newBet);
        AuctionResponseDto auctionResponseDto = auctionService.addBet(newBet);

        messagingTemplate.convertAndSend("/topic/auction/" + auctionResponseDto.getId(), auctionResponseDto);
    }
}
