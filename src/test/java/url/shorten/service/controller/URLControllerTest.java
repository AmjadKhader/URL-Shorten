package url.shorten.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import url.shorten.service.exception.InvalidURLException;
import url.shorten.service.exception.URLNotFoundException;
import url.shorten.service.service.URLService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class URLControllerTest {
    private MockMvc mockMvc;

    @Mock
    private URLService urlServiceMock;

    @InjectMocks
    private URLController urlController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(urlController)
                .setControllerAdvice(new ErrorController())
                .build();
    }

    @Test
    public void testShortenUrl_ValidUrl_ReturnsShortUrlResponse() throws Exception {
        String longUrl = "https://example.com/path";
        String shortUrl = "shortUrl";

        when(urlServiceMock.shortenUrl(longUrl)).thenReturn(shortUrl);

        mockMvc.perform(post("/url-shorten-service/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"" + longUrl + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("url").value(longUrl))
                .andExpect(jsonPath("shortUrl").value(shortUrl))
                .andReturn();

        verify(urlServiceMock, times(1)).shortenUrl(any());
        verifyNoMoreInteractions(urlServiceMock);
    }

    @Test
    public void testShortenUrl_InvalidUrl_ReturnsBadRequest() throws Exception {
        String invalidUrl = "invalidUrl";
        when(urlServiceMock.shortenUrl(invalidUrl)).thenThrow(new InvalidURLException("Invalid URL format"));

        mockMvc.perform(post("/url-shorten-service/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"" + invalidUrl + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid URL format"));

        verify(urlServiceMock, times(1)).shortenUrl(any());
        verifyNoMoreInteractions(urlServiceMock);
    }

    @Test
    public void testExpandUrl_ExistingShortUrl_ReturnsExpandUrlResponse() throws Exception {
        String shortUrl = "existingShortUrl";
        String longUrl = "https://www.example.com/path";

        when(urlServiceMock.expandUrl(shortUrl)).thenReturn(longUrl);

        mockMvc.perform(get("/url-shorten-service/api/expand?url=" + shortUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("url").value(longUrl))
                .andReturn();

        verify(urlServiceMock, times(1)).expandUrl(shortUrl);
        verifyNoMoreInteractions(urlServiceMock);
    }

    @Test
    public void testExpandUrl_NonExistingShortUrl_ReturnsNotFound() throws Exception {
        String nonExistingShortUrl = "nonExistingShortUrl";
        when(urlServiceMock.expandUrl(nonExistingShortUrl)).thenThrow(new URLNotFoundException("Short URL not found"));

        mockMvc.perform(get("/url-shorten-service/api/expand?url=" + nonExistingShortUrl))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Short URL not found"));

        verify(urlServiceMock, times(1)).expandUrl(nonExistingShortUrl);
        verifyNoMoreInteractions(urlServiceMock);
    }
}

