package de.allcom.repositories;

import de.allcom.models.Product;
import de.allcom.models.PhotoProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoProductRepository extends JpaRepository<PhotoProduct, Long> {
    List<PhotoProduct> findByProduct(Product product);

    void deleteAllByLink(String link);
    void deleteByLink(String link);

    void deleteAllByProduct(Product product);
}
