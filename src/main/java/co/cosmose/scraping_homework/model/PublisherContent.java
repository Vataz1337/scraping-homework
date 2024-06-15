package co.cosmose.scraping_homework.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherContent {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String articleUrl;

    private String title;

    private String author;

    @Column(columnDefinition="TEXT")
    private String htmlContent;

    @Column(columnDefinition="TEXT")
    private String originalContent;

    private String mainImageUrl;
}
