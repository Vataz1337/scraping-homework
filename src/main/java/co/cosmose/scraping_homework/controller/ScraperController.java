package co.cosmose.scraping_homework.controller;

import co.cosmose.scraping_homework.model.PublisherContent;
import co.cosmose.scraping_homework.service.RSSScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scrapper")
public class ScraperController {
    private final RSSScraperService rssScraperService;

    public ScraperController(RSSScraperService rssScraperService) {
        this.rssScraperService = rssScraperService;
    }

    @PostMapping("/scrapLink")
    public void scrapeUrl(@RequestParam String url) {
        //https://connect.thairath.co.th/ws/kaikai/content/mirror
        rssScraperService.scrapeAndSave(url);
    }

    @GetMapping("/getScrappedContent")
    public ResponseEntity<List<PublisherContent>> getScrappedContent() {
        List<PublisherContent> publisherContents = rssScraperService.getScrappedContent();
        return ResponseEntity.ok(publisherContents);
    }
}
