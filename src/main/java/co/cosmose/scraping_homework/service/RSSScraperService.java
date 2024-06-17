package co.cosmose.scraping_homework.service;

import co.cosmose.scraping_homework.exeption.RSSFeedException;
import co.cosmose.scraping_homework.model.PublisherContent;
import co.cosmose.scraping_homework.repository.PublisherContentRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RSSScraperService {
    private final PublisherContentRepository publisherContentRepository;
    private final SyndFeedInput syndFeedInput;

    public RSSScraperService(PublisherContentRepository publisherContentRepository, SyndFeedInput syndFeedInput) {
        this.publisherContentRepository = publisherContentRepository;
        this.syndFeedInput = syndFeedInput;
    }

    public List<PublisherContent> getScrappedContent() {
        return publisherContentRepository.findAll();
    }

    public void scrapeAndSave(String url) {
        SyndFeed feed = extractRssFromXml(url);

        if (feed == null) {
            log.info("Feed is null");
            return;
        }

        List<SyndEntry> entries = feed.getEntries();

        List<PublisherContent> contentList = entries.stream()
                .filter(entry -> !checkForDuplicate(entry.getLink(), entry.getTitle()))
                .map(this::createPublisherContentFromEntry)
                .collect(Collectors.toList());

        if (!contentList.isEmpty()) {
            publisherContentRepository.saveAll(contentList);
        }
    }

    SyndFeed extractRssFromXml(String url) {
        try {
            URI feedUri = new URI(url);
            URL feedUrl = feedUri.toURL();
            URLConnection connection =  feedUrl.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            return syndFeedInput.build(new XmlReader(inputStream));
        } catch (Exception e) {
            throw new RSSFeedException("Failed to extract RSS feed from URL: " + url, e);
        }
    }

    boolean checkForDuplicate(String articleUrl, String articleTitle) {
        return publisherContentRepository.existsByArticleUrlAndTitle(articleUrl, articleTitle);
    }

    String removeLinksFromHtml(String content) {
        try {
            String unescapedString = StringEscapeUtils.unescapeHtml4(content);
            Document document = Jsoup.parse(unescapedString);
            document.select("a").remove();

            return document.body().html();
        } catch (Exception e) {
            throw new RSSFeedException("Failed to parse HTML content", e);
        }
    }

    String extractMainImageUrl(SyndEntry entry) {
        return entry.getForeignMarkup().stream()
                .filter(element -> "thumbnail".equals(element.getName()) && "media".equals(element.getNamespacePrefix()))
                .map(element -> element.getAttributeValue("url"))
                .findFirst()
                .orElse(null);
    }

    private PublisherContent createPublisherContentFromEntry(SyndEntry entry) {
        String cleanedContent = removeLinksFromHtml(entry.getDescription().getValue());
        String image = extractMainImageUrl(entry);

        return PublisherContent.builder()
                .articleUrl(entry.getLink())
                .title(entry.getTitle())
                .author(entry.getAuthor())
                .htmlContent(cleanedContent)
                .mainImageUrl(image)
                .originalContent(entry.getDescription().getValue())
                .build();
    }
}