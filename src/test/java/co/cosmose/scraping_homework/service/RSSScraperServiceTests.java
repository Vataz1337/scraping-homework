package co.cosmose.scraping_homework.service;

import co.cosmose.scraping_homework.repository.PublisherContentRepository;
import com.rometools.rome.feed.synd.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class RSSScraperServiceTest {

    @Mock
    private PublisherContentRepository publisherContentRepository;

    @InjectMocks
    private RSSScraperService rssScraperService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void testScrapeAndSave() {
        // Mock input data
        SyndFeed syndFeed = mock(SyndFeed.class);
        List<SyndEntry> entries = new ArrayList<>();
        SyndEntry entry1 = mock(SyndEntry.class);
        when(entry1.getLink()).thenReturn("https://example.com/article1");
        when(entry1.getTitle()).thenReturn("Article 1");
        when(entry1.getAuthor()).thenReturn("Author 1");
        when(entry1.getDescription()).thenReturn(new SyndContentImpl());
        entries.add(entry1);

        when(syndFeed.getEntries()).thenReturn(entries);

        when(publisherContentRepository.existsByArticleUrlAndTitle("https://example.com/article1", "Article 1"))
                .thenReturn(false);

        RSSScraperService spyService = spy(rssScraperService);
        doReturn(syndFeed).when(spyService).extractRssFromXml(anyString());

        spyService.scrapeAndSave("https://example.com/rss");

        verify(publisherContentRepository, times(1)).saveAll(anyList());
    }

//    @Test
//    void testExtractRssFromXml() {
//        // You can write a test to check if RSSFeedException is thrown correctly
//        assertThrows(RSSFeedException.class, () -> {
//            rssScraperService.extractRssFromXml("invalid-url");
//        });
//    }
//
//    @Test
//    void testCheckForDuplicate() {
//        // Mock repository behavior
//        when(publisherContentRepository.existsByArticleUrlAndTitle("https://example.com/article1", "Article 1"))
//                .thenReturn(true);
//
//        // Call method under test
//        boolean result = rssScraperService.checkForDuplicate("https://example.com/article1", "Article 1");
//
//        // Verify result
//        assertTrue(result);
//    }
//
//    @Test
//    void testRemoveLinksFromHtml() {
//        String htmlContent = "<p>This is a test <a href='https://example.com'>link</a>.</p>";
//        String result = rssScraperService.removeLinksFromHtml(htmlContent);
//        assertEquals("This is a test .", Jsoup.parse(result).text());
//    }
//
//    @Test
//    void testExtractMainImageUrl() {
//        SyndEntry entry = mock(SyndEntry.class);
//        List<Element> foreignMarkup = new ArrayList<>();
//        Element element = new Element("thumbnail", "media");
//        element.setAttribute("url", "https://example.com/image.jpg");
//        foreignMarkup.add(element);
//
//        when(entry.getForeignMarkup()).thenReturn(foreignMarkup);
//
//        String result = rssScraperService.extractMainImageUrl(entry);
//        assertEquals("https://example.com/image.jpg", result);
//    }
}
