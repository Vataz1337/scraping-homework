package co.cosmose.scraping_homework.repository;

import co.cosmose.scraping_homework.model.PublisherContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PublisherContentRepository extends JpaRepository<PublisherContent, UUID> {
    boolean existsByArticleUrlAndTitle(String articleUrl, String articleTitle);
}
