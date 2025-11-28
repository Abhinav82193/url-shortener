package com.abhinav.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "urls", indexes = @Index(name = "idx_shortcode", columnList = "shortCode"))
@Data
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 10)
    private String shortCode;

    private Long clicks = 0L;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt; // null = never expire
}