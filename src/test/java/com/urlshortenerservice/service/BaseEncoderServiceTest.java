package com.urlshortenerservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BaseEncoderServiceTest {
    private BaseEncoderService baseEncoderService;

    @BeforeEach
    void setUp() {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        baseEncoderService = new BaseEncoderService();
        baseEncoderService.setAlphabet(alphabet);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "10, A",
            "62, 10",
            "12345, 3D7"
    })
    void encodeWithSpecificValuesTest(long input, String expectedEncoded) {
        String result = baseEncoderService.encode(input);
        assertEquals(expectedEncoded, result);
    }
}