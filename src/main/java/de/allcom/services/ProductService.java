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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;


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

    @Transactional
    public ProductDto createProductWithPhotos(CreateProductRequestDto request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setCategory(request.getCategory());
        product.setUpdateAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        if (!request.getImages().isEmpty()) {
            List<ProductImage> photos = productImageService.uploadPhotos(request.getImages(), savedProduct);
            savedProduct.setImages(photos);
        } else {
            System.out.println("net photok!");
        }
        savedProduct = productRepository.save(savedProduct);
        return converters.fromProductToProductDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(UpdateProductRequestDto request, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new RestException(HttpStatus.BAD_REQUEST, "The product is not found"));
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new RestException(HttpStatus.BAD_REQUEST, "The category is not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setColor(request.getColor());
        product.setCategory(category);

        if(!productImageService.getProductImages(product).isEmpty()) {
            List<String> altPhotos = product.getImages().stream().map(ProductImage::getLink).toList();
            for (String photo: altPhotos) {
                try {
                    Files.delete(Path.of(photo));
                } catch (IOException e) {
                    throw new RuntimeException("File is not copy");
                }
            }
            product.getImages().clear();
            productImageService.deleteImagesOfProduct(product);
        }
        if (request.getImages() != null) {
            List<ProductImage> newPhoto = productImageService.uploadPhotos(request.getImages(), product);
            product.setImages(newPhoto);
        }
        product.setUpdateAt(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return converters.fromProductToProductDto(updatedProduct);
    }
}
