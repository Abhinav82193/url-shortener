package com.abhinav.urlshortener.controller;

import com.abhinav.urlshortener.entity.UrlEntity;
import com.abhinav.urlshortener.service.UrlService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public String shorten(@RequestBody @NotBlank String originalUrl) {
        UrlEntity entity = urlService.shortenUrl(originalUrl);
        return "http://localhost:8080/" + entity.getShortCode();  
    }
}
