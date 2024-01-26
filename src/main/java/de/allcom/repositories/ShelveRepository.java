package de.allcom.repositories;

import de.allcom.models.Shelve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelveRepository extends JpaRepository<Shelve, Long> {
    Shelve findByNumber(Integer shelve);
}
