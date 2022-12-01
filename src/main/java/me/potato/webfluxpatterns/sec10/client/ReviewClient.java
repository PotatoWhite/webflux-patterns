package me.potato.webfluxpatterns.sec10.client;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec10.dto.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ReviewClient {
    private final WebClient client;

    public ReviewClient(@Value("${sec10.review.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @RateLimiter(name = "review-service", fallbackMethod = "fallback")
    public Mono<List<Review>> getReviews(Integer id) {
        return this.client.get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(Review.class)
                .collectList();
    }

    public Mono<List<Review>> fallback(Integer id, Throwable t) {
        log.error("fallback: {}", t.getMessage());
        return Mono.just(Collections.emptyList());
    }
}
