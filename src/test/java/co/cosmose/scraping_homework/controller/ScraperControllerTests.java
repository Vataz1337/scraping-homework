package co.cosmose.scraping_homework.controller;

import co.cosmose.scraping_homework.model.PublisherContent;
import co.cosmose.scraping_homework.service.RSSScraperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(ScraperController.class)
@AutoConfigureMockMvc
public class ScraperControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RSSScraperService rssScraperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScrapeUrl() throws Exception {
        String url = "https://example.com/rss";

        mockMvc.perform(MockMvcRequestBuilders.post("/scrapper/scrapLink")
                        .param("url", url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetScrappedContent() throws Exception {
        PublisherContent content1 = PublisherContent.builder()
                .id(UUID.randomUUID())
                .articleUrl("https://example.com/article/1")
                .title("Title 1")
                .author("Author 1")
                .htmlContent("<p>HTML Content 1</p>")
                .originalContent("Original Content 1")
                .mainImageUrl("https://example.com/image1.jpg")
                .build();

        PublisherContent content2 = PublisherContent.builder()
                .id(UUID.randomUUID())
                .articleUrl("https://example.com/article/2")
                .title("Title 2")
                .author("Author 2")
                .htmlContent("<p>HTML Content 2</p>")
                .originalContent("Original Content 2")
                .mainImageUrl("https://example.com/image2.jpg")
                .build();

        List<PublisherContent> mockContents = Arrays.asList(content1, content2);

        Mockito.when(rssScraperService.getScrappedContent()).thenReturn(mockContents);

        mockMvc.perform(MockMvcRequestBuilders.get("/scrapper/getScrappedContent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(mockContents.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].articleUrl").value("https://example.com/article/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Title 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("Author 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].articleUrl").value("https://example.com/article/2"));
    }
}