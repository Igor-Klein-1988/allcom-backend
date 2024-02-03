package de.allcom.repositories;

import de.allcom.models.Product;
import de.allcom.models.ProductImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProduct(Product product);

    Optional<ProductImage> deleteByLink(String link);
}

