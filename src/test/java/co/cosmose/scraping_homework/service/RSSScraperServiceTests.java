package co.cosmose.scraping_homework.service;

import co.cosmose.scraping_homework.exeption.RSSFeedException;
import co.cosmose.scraping_homework.repository.PublisherContentRepository;
import com.rometools.rome.feed.synd.*;

import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RSSScraperServiceTests {
    @Mock
    private SyndFeedInput syndFeedInput;

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
        SyndFeed syndFeed = mock(SyndFeed.class);
        List<SyndEntry> entries = new ArrayList<>();
        SyndEntry entry1 = mock(SyndEntry.class);

        when(entry1.getLink()).thenReturn("https://example.com/article1");
        when(entry1.getTitle()).thenReturn("Article 1");
        when(entry1.getAuthor()).thenReturn("Author 1");

        SyndContentImpl description = mock(SyndContentImpl.class);
        when(description.getValue()).thenReturn("<p>Some content</p>");
        when(entry1.getDescription()).thenReturn(description);

        List<Element> foreignMarkup = new ArrayList<>();
        Element thumbnailElement = new Element("thumbnail", "media")
                .setAttribute("url", "https://example.com/image.jpg");
        foreignMarkup.add(thumbnailElement);

        when(entry1.getForeignMarkup()).thenReturn(foreignMarkup);

        entries.add(entry1);

        when(syndFeed.getEntries()).thenReturn(entries);
        when(publisherContentRepository.existsByArticleUrlAndTitle("https://example.com/article1", "Article 1"))
                .thenReturn(false);

        RSSScraperService spyService = spy(rssScraperService);
        doReturn(syndFeed).when(spyService).extractRssFromXml(anyString());

        spyService.scrapeAndSave("https://example.com/rss");

        verify(publisherContentRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testExtractRssFromXml_Success() throws Exception {
        URI feedUri = new URI("https://connect.thairath.co.th/ws/kaikai/content/mirror");
        URL feedUrl = mock(URL.class);
        URLConnection connection = mock(URLConnection.class);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("some-rss.xml");
        SyndFeed mockSyndFeed = mock(SyndFeed.class);

        when(feedUrl.openConnection()).thenReturn(connection);
        when(connection.getInputStream()).thenReturn(inputStream);
        when(syndFeedInput.build(any(XmlReader.class))).thenReturn(mockSyndFeed);

        SyndFeed syndFeed = rssScraperService.extractRssFromXml(feedUri.toString());

        verify(syndFeedInput).build(any(XmlReader.class));

        assertNotNull(syndFeed);
    }

    @Test
    void testExtractRssFromXmlException() {
        assertThrows(RSSFeedException.class, () -> rssScraperService.extractRssFromXml("invalid-url"));
    }

    @Test
    void testCheckForDuplicate() {
        when(publisherContentRepository.existsByArticleUrlAndTitle("https://example.com/article1", "Article 1"))
                .thenReturn(true);

        boolean result = rssScraperService.checkForDuplicate("https://example.com/article1", "Article 1");

        assertTrue(result);
    }

    @Test
    void testRemoveLinksFromHtml() {
        String htmlContent = "This is a test <a href='https://example.com'>link</a>.";
        String result = rssScraperService.removeLinksFromHtml(htmlContent);
        assertEquals("This is a test .", Jsoup.parse(result).text());
    }

    @Test
    void testExtractMainImageUrl() {
        SyndEntry entry = mock(SyndEntry.class);

        Element element = new Element("thumbnail", "media", "https://example.com/image.jpg");
        element.setAttribute("url", "https://example.com/image.jpg");

        List<Element> foreignMarkup = new ArrayList<>();
        foreignMarkup.add(element);

        when(entry.getForeignMarkup()).thenReturn(foreignMarkup);

        String result = rssScraperService.extractMainImageUrl(entry);
        assertEquals("https://example.com/image.jpg", result);
    }
}