package com.vecondev.buildoptima.repository.faq;

import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import com.vecondev.buildoptima.model.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqQuestionRepository extends JpaRepository<FaqQuestion, UUID>,
    JpaSpecificationExecutor<FaqQuestion> {
  Boolean existsByQuestionIgnoreCase(String question);

  Optional<FaqQuestion> findTopByOrderByUpdatedAtDesc();

  Long countByStatus(Status status);

  @Query("""
        SELECT DISTINCT u FROM FaqQuestion q
        JOIN User u ON u.id=q.updatedBy
        WHERE  q.status= :status
        """)
  List<User> findDistinctModifiers(Status status);

  @Query("""
        SELECT DISTINCT c FROM FaqQuestion q
        JOIN FaqCategory c ON c.id=q.category
        WHERE  q.status= :status
        """)
  List<FaqCategory> findDistinctCategories(Status status);
}

