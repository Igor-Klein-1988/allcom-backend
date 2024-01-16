package de.allcom.repositories;

import de.allcom.models.Address;
import de.allcom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByUser(User savedUser);
}
