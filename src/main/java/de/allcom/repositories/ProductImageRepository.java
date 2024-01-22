package de.allcom.repositories;

import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProduct(Product product);
    void deleteAllByProduct(Product product);
}
