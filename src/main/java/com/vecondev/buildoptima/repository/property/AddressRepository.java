package com.vecondev.buildoptima.repository.property;

import com.vecondev.buildoptima.model.property.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {}
