package me.potato.webfluxpatterns.sec08.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec08.dto.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ReviewClient {
    private final WebClient client;

    public ReviewClient(@Value("${sec08.review.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @CircuitBreaker(name = "review-service", fallbackMethod = "fallBackReview")
    public Mono<List<Review>> getReviews(Integer id) {
        return this.client.get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(Review.class)
                .collectList()
                .retry(5)
                .timeout(Duration.ofMillis(300));
    }

    public Mono<List<Review>> fallBackReview(Integer id, Throwable t) {
        log.error("fallBackReview: {}", t.getMessage());
        return Mono.just(Collections.emptyList());
    }
}
