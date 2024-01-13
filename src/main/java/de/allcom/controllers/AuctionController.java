package de.allcom.controllers;

import de.allcom.controllers.api.AuctionsApi;
import de.allcom.dto.AuctionRequestDto;
import de.allcom.dto.AuctionResponseDto;
import de.allcom.dto.NewBetDto;
import de.allcom.services.AuctionService;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuctionController implements AuctionsApi {
    private final AuctionService auctionService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/makeBet")
    public void processBet(@Payload NewBetDto newBet) {
        System.out.println("NewBetDto: " + newBet);
        AuctionResponseDto auctionResponseDto = auctionService.addBet(newBet);

        // Отправляем обновление всем подписчикам данного аукциона
        messagingTemplate.convertAndSend("/topic/auction/" + auctionResponseDto.getId(), auctionResponseDto);
    }

    @Override
    public AuctionResponseDto create(AuctionRequestDto request) {
        return auctionService.create(request);
    }
}
