package me.potato.webfluxpatterns.sec01.client;

import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec01.dto.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ReviewClient {
    private final WebClient client;

    public ReviewClient(@Value("${sec01.review.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<List<Review>> getReviews(Integer id) {
        return this.client.get()
                .uri("{id}", id)
                .retrieve()
                .bodyToFlux(Review.class)
                .doOnError(e -> log.error("Error while getting reviews {}", id, e))
                .collectList()
                .onErrorReturn(Collections.emptyList());
    }
}
