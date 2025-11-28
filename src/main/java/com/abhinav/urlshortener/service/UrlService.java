package com.abhinav.urlshortener.service;

import com.abhinav.urlshortener.entity.UrlEntity;
import com.abhinav.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private static final Duration CACHE_TTL = Duration.ofDays(30);  // 30 days cache

    // 8 character short code
    public String generateShortCode() {
        byte[] buffer = new byte[6];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
    }

    @Transactional
    public UrlEntity shortenUrl(String originalUrl) {
        String shortCode;
        do {
            shortCode = generateShortCode();
        } while (urlRepository.existsByShortCode(shortCode));

        UrlEntity entity = new UrlEntity();
        entity.setOriginalUrl(originalUrl);
        entity.setShortCode(shortCode);
        entity.setClicks(0L);

        UrlEntity saved = urlRepository.save(entity);

        // Cache in Redis: key = shortCode, value = originalUrl
        redisTemplate.opsForValue().set(shortCode, originalUrl, CACHE_TTL);

        return saved;
    }

    public String getOriginalUrl(String shortCode) {
        // Pehle Redis se try karo (super fast)
        String cached = redisTemplate.opsForValue().get(shortCode);
        if (cached != null) {
            return cached;
        }

        // Agar Redis mein nahi mila to DB se
        UrlEntity entity = urlRepository.findByShortCode(shortCode).orElse(null);
        if (entity != null) {
            redisTemplate.opsForValue().set(shortCode, entity.getOriginalUrl(), CACHE_TTL);
            return entity.getOriginalUrl();
        }
        return null;
    }

    @Transactional
    public void incrementClicks(String shortCode) {
        urlRepository.findByShortCode(shortCode).ifPresent(url -> {
            url.setClicks(url.getClicks() + 1);
            urlRepository.save(url);
        });
    }
}