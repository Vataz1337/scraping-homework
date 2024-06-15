package co.cosmose.scraping_homework.controller;

import co.cosmose.scraping_homework.service.RSSScraperService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrapper")
public class ScraperController {
    private final RSSScraperService rssScraperService;

    public ScraperController(RSSScraperService rssScraperService) {
        this.rssScraperService = rssScraperService;
    }

    @PostMapping("/scrapLink")
    public void getFeed(@RequestParam String url) {
        //https://connect.thairath.co.th/ws/kaikai/content/mirror
        rssScraperService.scrapeAndSave(url);
    }
}
