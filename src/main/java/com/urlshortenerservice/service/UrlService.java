package com.urlshortenerservice.service;

import com.urlshortenerservice.cache.HashCache;
import com.urlshortenerservice.dto.UrlRequestDto;
import com.urlshortenerservice.dto.UrlResponseDto;
import com.urlshortenerservice.entity.Url;
import com.urlshortenerservice.exception.EntityNotFoundException;
import com.urlshortenerservice.mapper.UrlMapper;
import com.urlshortenerservice.repository.url.UrlCacheRepository;
import com.urlshortenerservice.repository.url.UrlJpaRepository;
import com.urlshortenerservice.validator.DurationValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlService {
    @Value("${url.cache.initial.ttl}")
    private Duration initialTimeout;

    @Value("${url.cache.secondary.ttl}")
    private Duration secondaryTimeout;

    private final UrlMapper urlMapper;
    private final UrlJpaRepository urlRepository;
    private final HashCache hashCache;
    private final DurationValidator durationValidator;
    private final UrlCacheRepository urlCacheRepository;
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        durationValidator.validateDurationIsNotNull(initialTimeout);
        durationValidator.validateDurationIsNotNull(secondaryTimeout);
    }

    @Transactional
    public UrlResponseDto createShortUrl(UrlRequestDto urlRequestDto) {
        Url redirectUrl = urlMapper.toEntity(urlRequestDto);
        String hash = hashCache.getHash();
        redirectUrl.setHash(hash);
        Url savedUrl = urlRepository.save(redirectUrl);
        urlCacheRepository.save(savedUrl.getHash(), savedUrl.getUrl(), initialTimeout);
        return urlMapper.toResponseDto(savedUrl);
    }

    @Transactional
    public String getUrlByHash(String hash) {
        Optional<String> urlFromCacheOptional = urlCacheRepository.get(hash);

        if (urlFromCacheOptional.isPresent()) {
            return urlFromCacheOptional.get();
        }

        Object lock = locks.computeIfAbsent(hash, key -> new Object());
        synchronized (lock) {
            try {
                Optional<String> doubleCheckedCachedUrl = urlCacheRepository.get(hash);
                return doubleCheckedCachedUrl.orElseGet(() -> urlRepository.findByHash(hash)
                        .map(url -> {
                            String redirectUrl = url.getUrl();
                            urlCacheRepository.save(hash, redirectUrl, secondaryTimeout);
                            return redirectUrl;
                        })
                        .orElseThrow(() -> new EntityNotFoundException("Could not find this redirecting url")));
            } finally {
                locks.remove(hash);
            }
        }
    }
}