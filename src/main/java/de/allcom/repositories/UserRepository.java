package de.allcom.repositories;

import de.allcom.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT u, a FROM User u LEFT JOIN Address a ON u.id = a.user.id",
            countQuery = "SELECT COUNT(u) FROM User u LEFT JOIN Address a ON u.id = a.user.id")
    List<Object[]> findAllUsersWithAddresses(Pageable pageable);

    Optional<Object> findByEmail(String userEmail);
}
