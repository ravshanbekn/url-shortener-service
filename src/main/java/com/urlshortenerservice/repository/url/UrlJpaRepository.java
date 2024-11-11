package com.urlshortenerservice.repository.url;

import com.urlshortenerservice.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlJpaRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByHash(String hash);
}