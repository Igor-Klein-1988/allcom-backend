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

    @Value("${image.upload.path}")
    private String uploadPath;

    @Value("${image.upload.link}")
    private String uploadLink;

    public List<ProductImage> uploadImages(List<MultipartFile> images, Product product) {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                continue;
            }
            String originalFileName = image.getOriginalFilename();
            if (originalFileName != null) {
                String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                String uuid = UUID.randomUUID().toString();
                String newFileName = uuid + "." + extension;
                String newFilePath = uploadPath + "/" + product.getId() + "/" + newFileName;
                String newFileLink = uploadLink + "/" + product.getId() + "/" + newFileName;

                ProductImage productImage = ProductImage.builder().link(newFileLink).product(product).build();
                try {
                    createDirectoryIfNotExists(uploadPath + "/" + product.getId());
                    Files.copy(image.getInputStream(), Path.of(newFilePath), StandardCopyOption.REPLACE_EXISTING);
                    productImages.add(productImage);
                    productImageRepository.save(productImage);
                } catch (IOException e) {
                    throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Failed to save file: " + newFilePath + ", error " + e.getMessage());
                }
            } else {
                throw new RestException(HttpStatus.BAD_REQUEST, "Please, check your file name");
            }
        }
        return productImages;
    }

    public List<ProductImage> getProductImages(Product product) {
        return productImageRepository.findByProduct(product);
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        try {
            Files.createDirectories(Path.of(directoryPath));
        } catch (IOException e) {
            throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Failed to create directory: " + directoryPath + ", error " + e.getMessage());
        }
    }

    public void deleteImages(List<String> imagesToRemove) {
        for (String imagePath : imagesToRemove) {
            try {
                Files.delete(Path.of(imagePath));
            } catch (IOException e) {
                throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Failed to delete file: " + imagePath + ", error " + e.getMessage());
            }
            productImageRepository.deleteByLink(imagePath)
                                  .orElseThrow(() -> new RestException(HttpStatus.UNPROCESSABLE_ENTITY,
                                          "Failed to delete link: " + imagePath));
        }
    }
}
