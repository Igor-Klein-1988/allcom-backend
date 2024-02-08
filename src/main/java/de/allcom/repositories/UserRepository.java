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

    @Query(value = "SELECT u, a FROM User u LEFT JOIN Address a ON u.id = a.user.id "
            + "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) "
            + "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))",
            countQuery = "SELECT COUNT(u) FROM User u LEFT JOIN Address a ON u.id = a.user.id "
                    + "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) "
                    + "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) "
                    + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Object[]> searchUsersWithAddresses(String query, Pageable pageable);

    Optional<User> findByEmail(String userEmail);
}
