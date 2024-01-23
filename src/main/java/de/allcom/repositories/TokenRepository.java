package de.allcom.repositories;

import de.allcom.models.token.Token;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
        select t from Token t inner join User u on t.user.id = u.id
        where u.id = :userId and t.expired = false and t.revoked = false
            """)
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);
}
