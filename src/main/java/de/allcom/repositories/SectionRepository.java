package de.allcom.repositories;

import de.allcom.models.Section;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByNumber(Integer section);
}
