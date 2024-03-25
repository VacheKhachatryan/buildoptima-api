package com.vecondev.buildoptima.repository.property;

import com.vecondev.buildoptima.model.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, String> {

  boolean existsByAin(String ain);
}
