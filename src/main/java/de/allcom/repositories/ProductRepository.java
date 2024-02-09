package de.allcom.repositories;

import de.allcom.models.Product;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategoryIdInAndNameContainingIgnoreCaseOrderByCreatedAtDesc(Set<Long> ids, String name,
            Pageable pageable);

    Page<Product> findAllByCategoryIdInOrderByCreatedAtDesc(Set<Long> ids, Pageable pageable);

    @Query(value = "SELECT * FROM products p WHERE p.state = :state AND p.category_id IN :ids ORDER BY p.created_at "
            + "DESC LIMIT 1", nativeQuery = true)
    Optional<Product> findFirstByStateAndCategoryIdsInNative(@Param("state") String state, @Param("ids") Set<Long> ids);

    Page<Product> findAllByNameContainingIgnoreCaseOrderByCreatedAtDesc(String name, Pageable pageable);

    Page<Product> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
}
