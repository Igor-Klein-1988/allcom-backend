package de.allcom.services;

import de.allcom.dto.forms.ProductResponseValues;
import de.allcom.dto.product.SaveProductRequestDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Category;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import de.allcom.repositories.CategoryRepository;
import de.allcom.repositories.ProductRepository;
import de.allcom.repositories.StorageRepository;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final CategoryRepository categoryRepository;
    private final StorageRepository storageRepository;

    @Transactional
    public ProductResponseValues saveProduct(SaveProductRequestDto saveProductRequestDto) {

        if (saveProductRequestDto == null) {
            throw new RestException(HttpStatus.BAD_REQUEST, "SaveProductRequestDto is null");
        }
        Product product;

        if (saveProductRequestDto.getId() != null) {
            product = productRepository.findById(saveProductRequestDto.getId())
                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                            "Product with id: " + saveProductRequestDto.getId() + " did not found"));
        } else {
            product = new Product();
        }
        Category category = categoryRepository.findById(saveProductRequestDto.getCategoryId())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Category with id: " + saveProductRequestDto.getCategoryId() + " did not found"));

        product.setName(saveProductRequestDto.getName());
        product.setDescription(saveProductRequestDto.getDescription());
        product.setWeight(saveProductRequestDto.getWeight());
        product.setColor(saveProductRequestDto.getColor());
        product.setCategory(category);
        product.setStorage(storageRepository.findById(saveProductRequestDto.getStorageId())
                .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, "Storage id must be")));
        product.setState(Product.State.DRAFT);
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        List<MultipartFile> newImages = saveProductRequestDto.getImages();

        if (!newImages.isEmpty()) {
            List<ProductImage> images = productImageService.uploadPhotos(newImages, savedProduct);
            savedProduct.setImages(images);
        }

        if (savedProduct.getImages().isEmpty() && !saveProductRequestDto.getImageLinks().isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND,
                    "Links of photos did not found in the DB");
        }

        List<ProductImage> images = savedProduct.getImages();
        List<ProductImage> imagesForRemove = images.stream()
                .filter(i -> saveProductRequestDto.getImageLinks().contains(i.getLink()))
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
        for (ProductImage imageToRemove : imagesForRemove) {
            productImageService.deleteImageById(imageToRemove.getId());
        }
        Product newProduct = productRepository.save(savedProduct);

        Product updatedProduct = productRepository.findById(newProduct.getId())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "Product with id: " + newProduct.getId() + " did not found"));

        updatedProduct.setImages(productImageService.getProductImages(updatedProduct));

        return ProductResponseValues.from(updatedProduct);
    }

    public ProductResponseValues findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Product with id: "
                        + id + " did not found"));
        return ProductResponseValues.from(product);
    }

    public Page<ProductResponseValues> searchByCategoryOrName(Long categoryId, String searchQuery,
                                                              PageRequest pageRequest) {
        Page<Product> products;
        boolean isSearchQueryPresent = searchQuery != null && !searchQuery.isEmpty();

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST,
                            "Category with id: " + categoryId + " not found"));

            products = isSearchQueryPresent
                    ? productRepository.findAllByCategoryAndNameStartsWithIgnoreCase(category, searchQuery, pageRequest)
                    : productRepository.findAllByCategory(category, pageRequest);
            return products.map(ProductResponseValues::from);
        } else {
            products = isSearchQueryPresent
                    ? productRepository.findAllByNameStartsWithIgnoreCase(searchQuery, pageRequest)
                    : productRepository.findAll(pageRequest);
            return products.map(ProductResponseValues::from);
        }
    }
}
