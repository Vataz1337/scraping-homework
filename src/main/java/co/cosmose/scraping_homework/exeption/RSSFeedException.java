package co.cosmose.scraping_homework.exeption;

public class RSSFeedException extends RuntimeException {
    public RSSFeedException(String message, Throwable cause) {
        super(message, cause);
    }
}
