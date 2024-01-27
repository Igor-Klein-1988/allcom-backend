package de.allcom.repositories;

import de.allcom.models.Rack;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {
    Optional<Rack> findByNumber(Integer rack);
}
