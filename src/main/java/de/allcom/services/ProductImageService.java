package de.allcom.services;

import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import de.allcom.repositories.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    @Value("${photo.upload.path}")
    private String uploadPath;
    public List<ProductImage> uploadPhotos(List<MultipartFile> photos, Product product) {
        List<ProductImage> photoProducts = new ArrayList<>();

        for (MultipartFile photo : photos) {
            String productUploadPath = uploadPath + "/" + product.getId();
            createDirectoryIfNotExists(productUploadPath);

            String originalFileName = photo.getOriginalFilename();
            String extension;

            if (originalFileName != null) {
                extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            } else {
                throw new IllegalArgumentException("Null original file name");
            }
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + "." + extension;
            String newFilePath = productUploadPath + "/" + newFileName;

            ProductImage productImage = new ProductImage();
            productImage.setLink(newFilePath);
            productImage.setProduct(product);
            photoProducts.add(productImage);

            try {
                Files.copy(photo.getInputStream(), Path.of(newFilePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("File is not copy");
            }
            productImageRepository.save(productImage);
        }
        return photoProducts;
    }
    public List<ProductImage> getProductImages(Product product){
        return productImageRepository.findByProduct(product);
    }

    public void deleteImagesOfProduct(Product product){
        productImageRepository.deleteAllByProduct(product);
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        try {
            Files.createDirectories(Path.of(directoryPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + directoryPath, e);
        }
    }

}
