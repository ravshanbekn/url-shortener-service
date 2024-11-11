package com.urlshortenerservice.service;

import com.urlshortenerservice.repository.hash.HashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashGeneratorService {
    private final HashRepository hashRepository;
    private final BaseEncoderService baseEncoderService;

    @Async(value = "hashThreadPool")
    @Transactional
    public void generateBatch() {
        List<Long> uniqueValues = hashRepository.getUniqueValues();
        List<String> hashes = uniqueValues.stream()
                .map(baseEncoderService::encode)
                .toList();
        hashRepository.batchSave(hashes);
    }
}