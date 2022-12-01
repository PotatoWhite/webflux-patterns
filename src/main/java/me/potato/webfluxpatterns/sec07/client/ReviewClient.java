package me.potato.webfluxpatterns.sec07.client;

import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec07.dto.Review;
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

    public ReviewClient(@Value("${sec07.review.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<List<Review>> getReviews(Integer id) {
        return this.client.get()
                .uri("{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(Review.class)
                .collectList()
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .onErrorReturn(Collections.emptyList());
    }
}
