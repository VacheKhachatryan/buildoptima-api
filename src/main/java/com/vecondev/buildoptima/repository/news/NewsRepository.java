package com.vecondev.buildoptima.repository.news;

import com.vecondev.buildoptima.model.Status;
import com.vecondev.buildoptima.model.news.News;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NewsRepository extends JpaRepository<News, UUID>, JpaSpecificationExecutor<News> {

  News findTopByOrderByUpdatedAtDesc();

  long countByStatus(Status status);

  List<News> findAllByStatus(Status status);
}
