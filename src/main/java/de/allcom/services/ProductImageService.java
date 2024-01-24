package de.allcom.services;

import de.allcom.exceptions.RestException;
import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import de.allcom.repositories.ProductImageRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    @Value("${photo.upload.path}")
    private String uploadPath;

    public List<ProductImage> uploadPhotos(List<MultipartFile> photos, Product product) {
        if (photos == null || photos.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductImage> photoProducts = new ArrayList<>();
        for (MultipartFile photo : photos) {
            if (photo.isEmpty()) {
                continue;
            }
            String originalFileName = photo.getOriginalFilename();
            if (originalFileName != null) {
                String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                String uuid = UUID.randomUUID().toString();
                String newFileName = uuid + "." + extension;
                String newFilePath = uploadPath + "/" + product.getId() + "/" + newFileName;

                ProductImage productImage = ProductImage.builder()
                        .link(newFilePath)
                        .product(product)
                        .build();
                try {
                    createDirectoryIfNotExists(uploadPath + "/" + product.getId());
                    Files.copy(photo.getInputStream(), Path.of(newFilePath), StandardCopyOption.REPLACE_EXISTING);
                    photoProducts.add(productImage);
                    productImageRepository.save(productImage);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file: " + newFilePath, e);
                }
            } else {
                throw new RestException(HttpStatus.BAD_REQUEST, "Please, check your file name");
            }
        }
        return photoProducts;
    }

    public List<ProductImage> getProductImages(Product product) {
        return productImageRepository.findByProduct(product);
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        try {
            Files.createDirectories(Path.of(directoryPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + directoryPath, e);
        }
    }

    public void deleteImageById(Long id) {
        productImageRepository.deleteById(id);
    }
}
