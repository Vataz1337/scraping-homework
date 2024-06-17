package co.cosmose.scraping_homework.controller;

import co.cosmose.scraping_homework.service.RSSScraperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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



}