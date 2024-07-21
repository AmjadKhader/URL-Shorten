package url.shorten.service.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import url.shorten.service.service.URLService;

@RestController
@RequestMapping("/url-shorten-service/api/")
public class URLController {

    private final URLService URLService;

    public URLController(URLService URLService) {
        this.URLService = URLService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody UrlRequest request) {
        String shortUrl = URLService.shortenUrl(request.getUrl());
        return ResponseEntity.ok(new ShortenUrlResponse(request.getUrl(), shortUrl));
    }

    @GetMapping("/expand")
    public ResponseEntity<ExpandUrlResponse> expandUrl(@RequestParam String url) {
        String longUrl = URLService.expandUrl(url);
        return ResponseEntity.ok(new ExpandUrlResponse(longUrl));
    }

    @Data
    @NoArgsConstructor
    public static class UrlRequest {
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpandUrlResponse {
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortenUrlResponse {
        private String url;
        private String shortUrl;
    }

}
