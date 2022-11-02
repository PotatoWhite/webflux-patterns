package me.potato.webfluxpatterns.sec01.client;

import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec01.dto.PromotionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@Service
public class PromotionClient {
    private final WebClient         client;
    private final PromotionResponse noPromotion = PromotionResponse.of(-1, "No Promotion", 0.0, LocalDate.now());

    public PromotionClient(@Value("${sec01.promotion.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();


    }

    public Mono<PromotionResponse> getPromotion(Integer id) {
        return this.client.get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(PromotionResponse.class)
                .doOnError(e -> log.error("Error while getting promotion {}", id, e))
                .onErrorReturn(noPromotion);
    }
}
