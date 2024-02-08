package de.allcom.services;

import de.allcom.dto.auction.AuctionDto;
import de.allcom.dto.auction.AuctionRequestDto;
import de.allcom.dto.product.ProductCreateRequestDto;
import de.allcom.dto.product.ProductResponseDto;
import de.allcom.dto.product.ProductUpdateRequestDto;
import de.allcom.dto.product.ProductWithAuctionDto;
import de.allcom.dto.storage.StorageCreateDto;
import de.allcom.dto.storage.StorageDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Auction;
import de.allcom.models.Category;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import de.allcom.models.Storage;
import de.allcom.repositories.AuctionRepository;
import de.allcom.repositories.CategoryRepository;
import de.allcom.repositories.ProductImageRepository;
import de.allcom.repositories.ProductRepository;
import de.allcom.repositories.StorageRepository;
import de.allcom.services.utils.Converters;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    public static final long PARENT_CATEGORY_ID = 0L;
    private final AuctionRepository auctionRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final StorageRepository storageRepository;

    private final AuctionService auctionService;
    private final CategoryService categoryService;
    private final StorageService storageService;
    private final ProductImageService productImageService;

    private final Converters converters;

    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                                           .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                   "Product with id: " + id + " not found"));
        return converters.convertToProductResponseDto(product, null);
    }

    @Transactional
    public ProductResponseDto create(ProductCreateRequestDto productDto, AuctionRequestDto auctionDto,
            StorageCreateDto storageDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                                              .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                      "Category with id: " + productDto.getCategoryId()
                                                              + " not found"));
        Storage savedStorage = storageService.create(storageDto);
        LocalDateTime timeNow = LocalDateTime.now();
        Product.State state = auctionDto.getStartAt().isAfter(timeNow) ? Product.State.IN_STOCK : Product.State.SOLD;

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setWeight(productDto.getWeight());
        product.setColor(productDto.getColor());
        product.setCategory(category);
        product.setStorage(savedStorage);
        product.setState(state);
        product.setUpdatedAt(timeNow);
        product.setCreatedAt(timeNow);
        Product savedProduct = productRepository.save(product);

        Auction newAuction = auctionService.create(auctionDto, savedProduct.getId());
        product.setAuctions(new ArrayList<>(List.of(newAuction)));

        List<MultipartFile> newImages = productDto.getImages();
        if (newImages != null) {
            List<ProductImage> images = productImageService.uploadImages(newImages, savedProduct);
            savedProduct.setImages(images);
        }

        Product savedProductWithAllInfo = productRepository.save(savedProduct);
        savedStorage.setProduct(savedProductWithAllInfo);
        storageRepository.save(savedStorage);
        newAuction.setProduct(savedProductWithAllInfo);
        auctionRepository.save(newAuction);

        return converters.convertToProductResponseDto(savedProductWithAllInfo, null);
    }

    @Transactional
    public ProductResponseDto update(ProductUpdateRequestDto productDto, AuctionDto auctionDto, StorageDto storageDto) {
        Product product = productRepository.findById(productDto.getId())
                                           .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                   "Product with id: " + productDto.getId() + " not found"));
        final Category category = categoryRepository.findById(productDto.getCategoryId())
                                                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                            "Category with id: " + productDto.getCategoryId()
                                                                    + " not found"));
        if (!storageDto.getId().equals(product.getStorage().getId())) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "Wrong storageId: " + storageDto.getId() + " for productId: " + product.getId());
        }
        final Storage storage = storageService.update(storageDto);

        List<Auction> productAuctions = auctionRepository.findAllByProductId(product.getId());
        boolean auctionExists = productAuctions.stream()
                                               .anyMatch(auction -> auction.getId().equals(auctionDto.getId()));
        if (!auctionExists) {
            throw new RestException(HttpStatus.BAD_REQUEST,
                    "Wrong auctionId: " + auctionDto.getId() + " for productId: " + product.getId());
        }
        auctionService.update(auctionDto);

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setWeight(productDto.getWeight());
        product.setColor(productDto.getColor());
        product.setCategory(category);
        product.setStorage(storage);


        if (productDto.getImages() != null && !productDto.getImages().isEmpty()) {
            productImageService.uploadImages(productDto.getImages(), product);
        }

        if (productDto.getImagesToRemove() != null && !productDto.getImagesToRemove().isEmpty()) {
            productImageService.deleteImages(productDto.getImagesToRemove());
        }

        List<ProductImage> productImages = productImageRepository.findByProduct(product);
        product.setImages(productImages);
        product.setUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return converters.convertToProductResponseDto(savedProduct, null);
    }

    public Page<ProductResponseDto> searchByCategoryOrName(Long categoryId, String searchQuery,
            PageRequest pageRequest) {
        Page<Product> products;
        boolean isSearchQueryPresent = searchQuery != null && !searchQuery.isEmpty();

        if (categoryId != null) {
            Set<Long> categoryIds = categoryService.findAllSubcategoryIdsWithCurrentCategoryId(categoryId);
            if (categoryIds.isEmpty()) {
                throw new RestException(HttpStatus.BAD_REQUEST, "Category with id: " + categoryId + " not found");
            }
            products = isSearchQueryPresent
                    ? productRepository.findAllByCategoryIdInAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
                    categoryIds, searchQuery, pageRequest)
                    : productRepository.findAllByCategoryIdInOrderByCreatedAtDesc(categoryIds, pageRequest);
            return products.map(p -> converters.convertToProductResponseDto(p, null));
        } else {
            products = isSearchQueryPresent ? productRepository.findAllByNameContainingIgnoreCaseOrderByCreatedAtDesc(
                    searchQuery, pageRequest) : productRepository.findAllByOrderByCreatedAtDesc(pageRequest);
            return products.map(p -> converters.convertToProductResponseDto(p, null));
        }
    }

    public List<ProductWithAuctionDto> getOneItemPerCategory() {
        List<Product> products = new ArrayList<>();
        for (Category category : categoryRepository.findAllByParentId(PARENT_CATEGORY_ID)) {
            Set<Long> categoryIds = categoryService.findAllSubcategoryIdsWithCurrentCategoryId(category.getId());
            Optional<Product> product = productRepository.findFirstByStateAndCategoryIdsInNative(
                    Product.State.SOLD.toString(), categoryIds);
            if (product.isPresent()) {
                products.add(product.get());
            }
        }

        return products.stream().map(p -> converters.convertToProductWithAuctionDto(p)).toList();
    }
}
