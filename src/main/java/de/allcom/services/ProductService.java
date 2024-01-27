package de.allcom.services;

import de.allcom.dto.product.CreateProductRequestDto;
import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.UpdateProductRequestDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Category;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import de.allcom.repositories.CategoryRepository;
import de.allcom.repositories.ProductRepository;
import de.allcom.services.utils.Converters;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final CategoryRepository categoryRepository;
    private final Converters converters;

    public Page<ProductDto> getAllProducts(PageRequest pageRequest) {
        Page<Product> products = productRepository.findAll(pageRequest);
        return products.map(converters::fromProductToProductDto);
    }

    public ProductDto findById(Long id) {
        return converters.fromProductToProductDto(productRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Product whith id: "
                        + id + " did not found")));
    }

    @Transactional
    public ProductDto createProductWithPhotos(CreateProductRequestDto request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setCategory(request.getCategory());
        product.setState(Product.State.DRAFT);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ProductImage> photos = productImageService.uploadPhotos(request.getImages(), savedProduct);
            savedProduct.setImages(photos);
        } else {
            savedProduct.setImages(null);
        }

        savedProduct = productRepository.save(savedProduct);

        return converters.fromProductToProductDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(UpdateProductRequestDto request, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new RestException(HttpStatus.NOT_FOUND, "The product is not found"));
        if (product.getImages().isEmpty() && !request.getImageLinks().isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND, "Links of photos did not found in the DB");
        }

        List<ProductImage> images = product.getImages();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setColor(request.getColor());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new RestException(HttpStatus.NOT_FOUND, "The category is not found"));
        product.setCategory(category);
        product.setState(Product.State.DRAFT);
        product.setUpdateAt(LocalDateTime.now());

        List<ProductImage> imagesForRemove = images.stream()
                .filter(i -> request.getImageLinks().contains(i.getLink()))
                .toList();

        if (!imagesForRemove.isEmpty()) {
            for (ProductImage productImage : imagesForRemove) {
                images.remove(productImage);
                try {
                    Files.delete(Path.of(productImage.getLink()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        product.setImages(images);
        productRepository.save(product);

        for (ProductImage imageToRemove : imagesForRemove) {
            productImageService.deleteImageById(imageToRemove.getId());
        }

        productImageService.uploadPhotos(request.getImages(), product);
        productRepository.save(product);

        Product updatedProduct = productRepository.findById(request.getId())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Product did not found in the DB"));

        List<ProductImage> imagesNew = productImageService.getProductImages(updatedProduct);

        return ProductDto.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .categoryId(updatedProduct.getCategory().getId())
                .state(updatedProduct.getState().name())
                .photoLinks(imagesNew.stream().map(ProductImage::getLink).toList())
                .build();
    }
}
