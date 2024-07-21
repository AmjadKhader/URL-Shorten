package url.shorten.service.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import url.shorten.service.exception.InvalidURLException;
import url.shorten.service.exception.URLNotFoundException;
import url.shorten.service.model.Url;
import url.shorten.service.repo.URLRepository;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class URLService {

    private final URLRepository urlRepository;

    public URLService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String longUrl) {

        validateUrl(longUrl);

        longUrl = removeHTTPWWWandTrailingSlash(longUrl);
        Optional<Url> existingUrl = urlRepository.findByLongUrl(longUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        }

        String shortUrl = buildShortURL(longUrl);

        Url url = new Url(longUrl, shortUrl);
        urlRepository.save(url);

        return shortUrl;
    }

    public String expandUrl(String shortUrl) {
        Optional<Url> url = urlRepository.findByShortUrl(shortUrl);
        return url.orElseThrow(() -> new URLNotFoundException("Short URL not found")).getLongUrl();
    }

    private void validateUrl(String url) {
        if (Objects.isNull(url) || url.isBlank()) {
            throw new InvalidURLException("Invalid URL format");
        }

        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(url)) {
            throw new InvalidURLException("Invalid URL format");
        }
    }

    private String removeHTTPWWWandTrailingSlash(String longUrl) {
        String longUrlReplaceHTTPSAndWWW = longUrl.replaceFirst("http://", "https://")
                .replaceFirst("www\\.", "");
        return longUrlReplaceHTTPSAndWWW.endsWith("/") ? longUrlReplaceHTTPSAndWWW.substring(0, longUrlReplaceHTTPSAndWWW.length() - 1) : longUrlReplaceHTTPSAndWWW;
    }

    private String buildShortURL(String longUrl) {
        String shortUrl;
        String innerUrl = longUrl.substring(longUrl.indexOf("//") + 2);
        innerUrl = innerUrl.substring(0, innerUrl.lastIndexOf("."));

        if (innerUrl.length() > 2) {
            String prefix = innerUrl.substring(0, 3);
            String suffix = innerUrl.substring(innerUrl.length() - 2);

            shortUrl = "http://" + prefix + "." + suffix + "/" + getEncodedString(innerUrl);
        } else {
            // Handle short URLs
            shortUrl = "http://" + getEncodedString(innerUrl) + "/" + getEncodedString(innerUrl);
        }
        return shortUrl;
    }

    private String getEncodedString(String toEncode) {
        return Base64.getUrlEncoder().encodeToString((Math.random() + toEncode).getBytes())
                .substring(0, toEncode.length() + new Random().nextInt(10 - 4 + 1) + 4);
    }
}