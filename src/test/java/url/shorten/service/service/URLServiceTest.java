package url.shorten.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import url.shorten.service.exception.InvalidURLException;
import url.shorten.service.exception.URLNotFoundException;
import url.shorten.service.model.Url;
import url.shorten.service.repo.URLRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class URLServiceTest {

    @Mock
    private URLRepository urlRepositoryMock;

    @InjectMocks
    private URLService urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl_ValidUrl_ReturnsShortUrl() {
        String longUrl = "https://example.com/path";
        String expectedShortUrl = "shortUrl";

        Optional<Url> emptyOptional = Optional.empty();
        when(urlRepositoryMock.findByLongUrl(any())).thenReturn(emptyOptional);
        when(urlRepositoryMock.save(any(Url.class))).thenReturn(new Url(longUrl, expectedShortUrl));

        String shortUrl = urlService.shortenUrl(longUrl);

        assertNotEquals("", shortUrl);
        verify(urlRepositoryMock, times(1)).findByLongUrl(any());
        verify(urlRepositoryMock, times(1)).save(any());
    }

    @Test
    public void testShortenUrl_ExistingUrl_ReturnsExistingShortUrl() {
        String longUrl = "https://example.com/path";
        String existingShortUrl = "existingShortUrl";
        Url existingUrl = new Url(longUrl, existingShortUrl);

        when(urlRepositoryMock.findByLongUrl(longUrl)).thenReturn(Optional.of(existingUrl));

        String shortUrl = urlService.shortenUrl(longUrl);

        assertEquals(existingShortUrl, shortUrl);
        verify(urlRepositoryMock, times(1)).findByLongUrl(any());
        verify(urlRepositoryMock, times(0)).save(any());
    }

    @Test
    public void testShortenUrl_NullUrl_ThrowsInvalidURLException() {
        assertThrows(InvalidURLException.class, () -> urlService.shortenUrl(null));
        verify(urlRepositoryMock, never()).findByLongUrl(any());
        verify(urlRepositoryMock, never()).save(any(Url.class));
    }

    @Test
    public void testShortenUrl_BlankUrl_ThrowsInvalidURLException() {
        assertThrows(InvalidURLException.class, () -> urlService.shortenUrl(" "));
        verify(urlRepositoryMock, never()).findByLongUrl(any());
        verify(urlRepositoryMock, never()).save(any(Url.class));
    }

    @Test
    public void testShortenUrl_InvalidUrlFormat_ThrowsInvalidURLException() {
        String invalidUrl = "invalidUrl";

        assertThrows(InvalidURLException.class, () -> urlService.shortenUrl(invalidUrl));
        verify(urlRepositoryMock, never()).findByLongUrl(any());
        verify(urlRepositoryMock, never()).save(any(Url.class));
    }

    @Test
    public void testExpandUrl_ExistingShortUrl_ReturnsLongUrl() {
        String shortUrl = "existingShortUrl";
        String longUrl = "https://www.example.com/path";
        Url existingUrl = new Url(longUrl, shortUrl);

        Optional<Url> existingUrlOptional = Optional.of(existingUrl);
        when(urlRepositoryMock.findByShortUrl(shortUrl)).thenReturn(existingUrlOptional);

        String retrievedLongUrl = urlService.expandUrl(shortUrl);

        assertEquals(longUrl, retrievedLongUrl);
        verify(urlRepositoryMock).findByShortUrl(shortUrl);
    }

    @Test
    public void testExpandUrl_ExistingShortUrl_URLNotFoundException() {
        String shortUrl = "existingShortUrl";

        assertThrows(URLNotFoundException.class, () -> urlService.expandUrl(shortUrl));
        verify(urlRepositoryMock, never()).findByLongUrl(any());
        verify(urlRepositoryMock, never()).save(any(Url.class));
    }

    @Test
    public void testExpandUrl_ExistingShortUrl_NullURL() {
        assertThrows(URLNotFoundException.class, () -> urlService.expandUrl(null));
        verify(urlRepositoryMock, never()).findByLongUrl(any());
        verify(urlRepositoryMock, never()).save(any(Url.class));
    }

}