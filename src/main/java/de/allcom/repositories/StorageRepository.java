package de.allcom.repositories;

import de.allcom.models.Storage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    Optional<Storage> findFirstByProductId(Long productId);
}
