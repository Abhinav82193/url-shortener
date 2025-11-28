package com.abhinav.urlshortener.controller;

import com.abhinav.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AnalyticsController {

    @Autowired
    private UrlRepository urlRepository;

    @GetMapping("/api/analytics")
    public Map<String, Object> getAnalytics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUrls", urlRepository.count());
        stats.put("totalClicks", urlRepository.findAll().stream()
                .mapToLong(url -> url.getClicks() != null ? url.getClicks() : 0)
                .sum());

        // Top 5 most clicked URLs
        stats.put("topUrls", urlRepository.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(b.getClicks() != null ? b.getClicks() : 0,
                                               a.getClicks() != null ? a.getClicks() : 0))
                .limit(5)
                .map(url -> Map.of(
                        "shortCode", url.getShortCode(),
                        "originalUrl", url.getOriginalUrl(),
                        "clicks", url.getClicks() != null ? url.getClicks() : 0
                ))
                .toList());

        return stats;
    }
}