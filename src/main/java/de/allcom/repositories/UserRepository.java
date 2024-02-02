package de.allcom.repositories;

import de.allcom.models.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u, a FROM User u LEFT JOIN Address a ON u.id = a.user.id",
            countQuery = "SELECT COUNT(u) FROM User u LEFT JOIN Address a ON u.id = a.user.id")
    Page<Object[]> findAllUsersWithAddresses(Pageable pageable);

    Optional<Object> findByEmail(String userEmail);
}
