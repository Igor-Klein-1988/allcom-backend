package de.allcom.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.allcom.controllers.api.ProductApi;
import de.allcom.dto.auction.AuctionDto;
import de.allcom.dto.auction.AuctionRequestDto;
import de.allcom.dto.product.ProductCreateRequestDto;
import de.allcom.dto.product.ProductResponseDto;
import de.allcom.dto.product.ProductUpdateRequestDto;
import de.allcom.dto.product.ProductWithAuctionDto;
import de.allcom.dto.storage.StorageCreateDto;
import de.allcom.dto.storage.StorageDto;
import de.allcom.exceptions.RestException;
import de.allcom.services.ProductService;
import de.allcom.validation.ValidationUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final ValidationUtil validationUtil;

    @Override
    public ProductResponseDto create(String auctionJson, String storageJson, ProductCreateRequestDto productDto) {
        try {
            AuctionRequestDto auctionDto = objectMapper.readValue(auctionJson, AuctionRequestDto.class);
            StorageCreateDto storageDto = objectMapper.readValue(storageJson, StorageCreateDto.class);
            validationUtil.validate(storageDto);
            validationUtil.validate(auctionDto);

            return productService.create(productDto, auctionDto, storageDto);
        } catch (JsonProcessingException e) {
            throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid request format: " + e.getMessage());
        }
    }

    @Override
    public ProductResponseDto update(String auctionJson, String storageJson, ProductUpdateRequestDto productDto) {
        try {
            AuctionDto auctionDto = objectMapper.readValue(auctionJson, AuctionDto.class);
            StorageDto storageDto = objectMapper.readValue(storageJson, StorageDto.class);
            validationUtil.validate(storageDto);
            validationUtil.validate(auctionDto);

            return productService.update(productDto, auctionDto, storageDto);
        } catch (JsonProcessingException e) {
            throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid request format: " + e.getMessage());
        }
    }


    @Override
    public ProductResponseDto findById(Long id) {
        return productService.findById(id);
    }

    @Override
    public Page<ProductResponseDto> searchByCategoryOrName(Long id, String searchQuery, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productService.searchByCategoryOrName(id, searchQuery, pageRequest);
    }

    @Override
    public List<ProductWithAuctionDto> getOneItemPerCategory() {
        return productService.getOneItemPerCategory();
    }
}
