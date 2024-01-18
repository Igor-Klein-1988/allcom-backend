package de.allcom.services;

import de.allcom.dto.product.CreateProductRequestDto;
import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.UpdateProductRequestDto;
import de.allcom.exceptions.CopyPhotoException;
import de.allcom.exceptions.NotFoundException;
import de.allcom.models.Category;
import de.allcom.models.Product;
import de.allcom.models.PhotoProduct;
import de.allcom.repositories.CategoryRepository;
import de.allcom.repositories.PhotoProductRepository;
import de.allcom.repositories.ProductRepository;
import de.allcom.services.utils.Converters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Value("${photo.upload.path}")
    private String uploadPath;

    private final ProductRepository productRepository;
    private final PhotoProductRepository photoProductRepository;
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
        Product savedProduct = productRepository.save(product);
        if (request.getPhotos() != null) {
            List<PhotoProduct> photos = uploadPhotos(request.getPhotos(), savedProduct);
            savedProduct.setPhotos(photos);
        } else {
            throw new IllegalArgumentException("Null original file name");
        }
        productRepository.save(savedProduct);
        return converters.fromProductToProductDto(savedProduct);
    }

    public List<PhotoProduct> uploadPhotos(List<MultipartFile> photos, Product product) {
        List<PhotoProduct> photoProducts = new ArrayList<>();
        if (photos.isEmpty()) {
            return new ArrayList<>();
        }
        for (MultipartFile photo : photos) {

            String originalFileName = photo.getOriginalFilename();
            String extension;

            if (originalFileName != null) {
                extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            } else {
                throw new IllegalArgumentException("Null original file name");
            }
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + "." + extension;
            String newFilePath = uploadPath + "/" + newFileName;

            PhotoProduct photoProduct = PhotoProduct.builder()
                    .link(newFilePath)
                    .build();
            try {
                Files.copy(photo.getInputStream(), Path.of(newFilePath), StandardCopyOption.REPLACE_EXISTING);
                photoProducts.add(photoProduct);
            } catch (IOException e) {
                try {
                    throw new CopyPhotoException(HttpStatus.BAD_REQUEST, "Error copy photo: " + originalFileName);
                } catch (CopyPhotoException ex) {
                    throw new RuntimeException(ex);
                }
            }
            photoProduct.setProduct(product);
            photoProductRepository.save(photoProduct);
        }
        return photoProducts;
    }

    @Transactional
    public ProductDto updateProduct(UpdateProductRequestDto request) {
        Product product = productRepository.findById(request.getId()).orElseThrow(() ->
                new NotFoundException(HttpStatus.BAD_REQUEST, "The product is not found"));
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                new NotFoundException(HttpStatus.BAD_REQUEST, "The category is not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWeight(request.getWeight());
        product.setColor(request.getColor());
        product.setCategory(category);
        if(!photoProductRepository.findByProduct(product).isEmpty()) {
            List<String> altPhotos = product.getLinksOfPhotos();
            for (String photo: altPhotos) {
                try {
                    Files.delete(Path.of(photo));
                } catch (IOException e) {
                    try {
                        throw new CopyPhotoException(HttpStatus.BAD_REQUEST, "Error copy photo: " + photo);
                    } catch (CopyPhotoException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            product.getPhotos().clear();
            photoProductRepository.deleteAllByProduct(product);
        }
        List<PhotoProduct> newPhoto = uploadPhotos(request.getPhotos(), product);
        product.setPhotos(newPhoto);
        Product updatedProduct = productRepository.save(product);
        return converters.fromProductToProductDto(updatedProduct);
    }
}
