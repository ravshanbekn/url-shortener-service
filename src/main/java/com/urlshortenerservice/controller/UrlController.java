package com.urlshortenerservice.controller;

import com.urlshortenerservice.dto.UrlRequestDto;
import com.urlshortenerservice.dto.UrlResponseDto;
import com.urlshortenerservice.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/short")
@Tag(name = "Url Controller", description = "Controller for handling url requests")
public class UrlController {
    private final UrlService urlService;

    @Operation(summary = "Create short url", description = "Enter url to shorten")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Short url was created", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UrlResponseDto.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid provided data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    @PostMapping
    public UrlResponseDto createShortUrl(@RequestBody @Valid UrlRequestDto urlRequestDto) {
        return urlService.createShortUrl(urlRequestDto);
    }

    @Operation(summary = "Get site by short url", description = "Enter short url")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Found and redirected to original url"),
            @ApiResponse(responseCode = "400", description = "Invalid short url was provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{hash}")
    public ResponseEntity<Void> getOriginalUrlByShortUrl(@PathVariable String hash) {
        String urlByHash = urlService.getUrlByHash(hash);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlByHash))
                .build();
    }
}