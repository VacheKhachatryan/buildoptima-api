package com.vecondev.buildoptima.repository.faq;

import com.vecondev.buildoptima.model.faq.FaqCategory;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqCategoryRepository
    extends JpaRepository<FaqCategory, UUID>, JpaSpecificationExecutor<FaqCategory> {

  Boolean existsByNameIgnoreCase(String name);

  Optional<FaqCategory> findTopByOrderByUpdatedAtDesc();

  @Query(value = """
                SELECT CASE 
                       WHEN count(c) = 0 THEN false
                       ELSE true 
                       END
                FROM FaqCategory c JOIN
                FaqQuestion q ON c.id = q.category
                WHERE c.id = :id
                """
  )
  Boolean hasAnyQuestion(UUID id);
}

