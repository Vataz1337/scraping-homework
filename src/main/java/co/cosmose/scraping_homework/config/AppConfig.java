package co.cosmose.scraping_homework.config;

import com.rometools.rome.io.SyndFeedInput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SyndFeedInput syndFeedInput() {
        return new SyndFeedInput();
    }
}