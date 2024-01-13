package de.allcom.services;

import de.allcom.dto.AuctionResponseDto;
import de.allcom.dto.NewBetDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Auction;
import de.allcom.models.Bet;
import de.allcom.models.Product;
import de.allcom.models.User;
import de.allcom.repositories.AuctionRepository;
import de.allcom.repositories.BetRepository;
import de.allcom.repositories.UsersRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auction Service:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class AuctionServiceTest {
    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private UsersRepository userRepository;
    @Mock
    private BetRepository betRepository;

    @Mock
    private BetService betService;

    @InjectMocks
    private AuctionService auctionService;

    @Nested
    @DisplayName("addBet():")
    public class AddBet {
        @Test
        public void failed_bet_addition_due_to_lower_amount() {
            // Настройка DTO для новой ставки
            NewBetDto newBetDto = NewBetDto.builder()
                                           .auctionId(1L)
                                           .userId(1L)
                                           .betAmount(450)
                                           .build();

            // Мокирование ответов репозиториев
            when(userRepository.findById(any())).thenReturn(Optional.of(new User(/* параметры */)));
            when(auctionRepository.findById(any())).thenReturn(Optional.of(Auction.builder()
                                                                                  .product(Product.builder()
                                                                                                  .id(1L)
                                                                                                  .build())
                                                                                  .startPrice(450)
                                                                                  .build()));
            Bet bet = Bet.builder()
                         .betAmount(500)
                         .build();
            when(betRepository.findFirstByAuctionIdOrderByIdDesc(any())).thenReturn(Optional.of(bet));
            // Ожидание исключения
            assertThrows(RestException.class, () -> {
                auctionService.addBet(newBetDto);
            });

            // Подтверждение, что методы репозиториев были вызваны
            verify(userRepository, times(1)).findById(any());
            verify(auctionRepository, times(1)).findById(any());
            verify(betRepository, times(1)).findFirstByAuctionIdOrderByIdDesc(any());
        }

        @Test
        public void successful_bet_addition() {
            NewBetDto newBetDto = NewBetDto.builder()
                                           .auctionId(1L)
                                           .userId(1L)
                                           .betAmount(550)
                                           .build();

            when(userRepository.findById(any())).thenReturn(Optional.of(new User(/* параметры */)));
            when(auctionRepository.findById(any())).thenReturn(Optional.of(Auction.builder()
                                                                                  .product(Product.builder()
                                                                                                  .id(1L)
                                                                                                  .build())
                                                                                  .startPrice(450)
                                                                                  .build()));
            Bet bet = Bet.builder()
                         .betAmount(500)
                         .build();
            when(betRepository.findFirstByAuctionIdOrderByIdDesc(any())).thenReturn(Optional.of(bet));
//            when(betService.createBet(new User(), new Auction(), 550)).thenReturn(new Bet());
            when(betService.createBet(any(User.class), any(Auction.class), anyInt())).thenReturn(
                    new Bet()); // Вернуть заглушку Bet вместо void
            // Вызов метода addBet
            AuctionResponseDto responseDto = auctionService.addBet(newBetDto);

            // Проверка результатов
            assertNotNull(responseDto);
            // Дополнительные проверки...

            // Подтверждение, что методы репозиториев были вызваны
            verify(userRepository, times(1)).findById(any());
            verify(auctionRepository, times(1)).findById(any());
            verify(betRepository, times(1)).findFirstByAuctionIdOrderByIdDesc(any());
        }
    }
}
