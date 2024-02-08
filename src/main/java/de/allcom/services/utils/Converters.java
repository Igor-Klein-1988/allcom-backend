package de.allcom.services.utils;

import de.allcom.dto.auction.AuctionDto;
import de.allcom.dto.product.ProductResponseDto;
import de.allcom.dto.storage.StorageDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Auction;
import de.allcom.models.Bet;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import de.allcom.models.Storage;
import de.allcom.repositories.AuctionRepository;
import de.allcom.repositories.BetRepository;
import de.allcom.repositories.StorageRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class Converters {
    private final AuctionRepository auctionRepository;
    private final BetRepository betRepository;
    private final StorageRepository storageRepository;

    public ProductResponseDto convertToProductResponseDto(Product product, Integer lastUsersBetAmount) {
        Storage storage = storageRepository.findFirstByProductId(product.getId())
                                           .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                   "Product with productId: " + product.getId()
                                                           + " did not have Storage"));
        Auction lastCreatedAuction = auctionRepository.findFirstByProductIdOrderByCreatedAtDesc(product.getId())
                                                      .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                              "Product with productId: " + product.getId()
                                                                      + " did not have Auctions"));

        Optional<Bet> maxBet = betRepository.findFirstByAuctionIdOrderByBetAmountDesc(lastCreatedAuction.getId());
        Integer currentPrice = maxBet.isPresent() ? maxBet.get().getBetAmount() : lastCreatedAuction.getStartPrice();

        List<String> imageLinks = product.getImages() != null ? product.getImages()
                                                                       .stream()
                                                                       .map(ProductImage::getLink)
                                                                       .toList() : null;
        return ProductResponseDto.builder()
                                 .id(product.getId())
                                 .name(product.getName())
                                 .description(product.getDescription())
                                 .weight(product.getWeight())
                                 .color(product.getColor())
                                 .categoryId(product.getCategory().getId())
                                 .state(product.getState().toString())
                                 .imageLinks(imageLinks)
                                 .lastCreatedAuction(AuctionDto
                                         .from(lastCreatedAuction, currentPrice, lastUsersBetAmount))
                                 .storage(StorageDto.from(storage))
                                 .build();
    }
}
