package de.allcom.repositories;

import de.allcom.models.Category;
import de.allcom.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategory(Category category,
                                    Pageable pageable);

    Page<Product> findAllByCategoryAndNameStartsWithIgnoreCase(Category category, String name, Pageable pageable);

    Page<Product> findAllByNameStartsWithIgnoreCase(String name, Pageable pageable);
}
