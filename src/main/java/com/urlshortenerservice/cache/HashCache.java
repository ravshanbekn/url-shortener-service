package com.urlshortenerservice.cache;

import com.urlshortenerservice.repository.hash.HashRepository;
import com.urlshortenerservice.service.HashGeneratorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class HashCache {
    @Value("${hash.cache.size}")
    private Integer cacheSize;
    @Value("${hash.cache.min-percentage}")
    private Integer cacheMinPercentage;

    private final HashGeneratorService hashGeneratorService;
    private final HashRepository hashRepository;
    private final ThreadPoolExecutor hashCacheThreadPool;
    private final ArrayBlockingQueue<String> hashCacheQueue;
    private final Lock lock = new ReentrantLock();

    @PostConstruct
    @Transactional
    public void init() {
        cacheHashes();
        System.out.println(hashCacheQueue);
    }

    public String getHash() {
        if ((double) hashCacheQueue.size() / cacheSize <= cacheMinPercentage) {
            cache();
        }
        return hashCacheQueue.poll();
    }

    public void cache() {
        hashCacheThreadPool.execute(() -> {
            if (lock.tryLock()) {
                try {
                    cacheHashes();
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    private void cacheHashes() {
        hashGeneratorService.generateBatch();
        hashRepository.getHashBatch().forEach(hashCacheQueue::offer);
    }
}