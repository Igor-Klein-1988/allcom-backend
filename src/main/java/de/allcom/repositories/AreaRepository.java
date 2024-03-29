package de.allcom.repositories;

import de.allcom.models.Area;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByName(Area.Name name);
}
