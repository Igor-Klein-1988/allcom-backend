package de.allcom.repositories;

import de.allcom.models.Product;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategoryIdInAndNameContainingIgnoreCase(Set<Long> ids, String name, Pageable pageable);

    Page<Product> findAllByCategoryIdIn(Set<Long> ids, Pageable pageable);

    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
