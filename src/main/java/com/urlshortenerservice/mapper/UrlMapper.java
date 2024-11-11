package com.urlshortenerservice.mapper;

import com.urlshortenerservice.dto.UrlRequestDto;
import com.urlshortenerservice.dto.UrlResponseDto;
import com.urlshortenerservice.entity.Url;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UrlMapper {

    Url toEntity(UrlRequestDto urlRequestDto);

    UrlResponseDto toResponseDto(Url url);
}