package com.urlshortenerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortenerservice.dto.UrlRequestDto;
import com.urlshortenerservice.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UrlControllerTest {
    @InjectMocks
    private UrlController urlController;
    @Mock
    private UrlService urlService;
    private MockMvc mockMvc;

    private UrlRequestDto urlRequestDto;
    private String urlRequestDtoJson;
    private String hash;
    private String redirectUrl;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        String url = "https://someurl";
        hash = "hash";
        redirectUrl = "redirectUrl";
        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
        urlRequestDto = UrlRequestDto.builder()
                .url(url)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        urlRequestDtoJson = objectMapper.writeValueAsString(urlRequestDto);

    }


    @Test
    @DisplayName("Testing creationShortUrl methods")
    void testCreateShortUrl() throws Exception {
        mockMvc.perform(post("/short")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(urlRequestDtoJson))
                .andExpect(status().isOk());

        verify(urlService, times(1)).createShortUrl(urlRequestDto);
    }

    @Test
    @DisplayName("Testing redirect methods")
    void test() throws Exception {
        when(urlService.getUrlByHash(hash)).thenReturn(redirectUrl);

        mockMvc.perform(get("/short/{hash}", hash))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", redirectUrl));

        verify(urlService, times(1)).getUrlByHash(hash);
    }
}