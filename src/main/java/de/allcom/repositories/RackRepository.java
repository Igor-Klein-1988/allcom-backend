package de.allcom.repositories;

import de.allcom.models.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {
    Rack findByNumber(Integer rack);
}
