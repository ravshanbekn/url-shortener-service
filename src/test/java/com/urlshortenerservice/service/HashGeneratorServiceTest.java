package com.urlshortenerservice.service;

import com.urlshortenerservice.repository.hash.HashRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashGeneratorServiceTest {
    @InjectMocks
    private HashGeneratorService hashGeneratorService;

    @Mock
    private HashRepository hashRepository;
    @Spy
    private BaseEncoderService baseEncoderService;

    private List<Long> uniqueValues;
    private List<String> hashes;

    @BeforeEach
    void setUp() {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        baseEncoderService.setAlphabet(alphabet);
        uniqueValues = List.of(1L, 2L, 3L, 4L, 5L);
        hashes = List.of("1", "2", "3", "4", "5");
    }

    @Test
    @DisplayName("testing generateBatch method by execution")
    void testGenerateBatch() {
        when(hashRepository.getUniqueValues()).thenReturn(uniqueValues);

        hashGeneratorService.generateBatch();

        verify(hashRepository, times(1)).getUniqueValues();
        verify(baseEncoderService, times(uniqueValues.size())).encode(anyLong());
        verify(hashRepository, times(1)).batchSave(hashes);

    }
}