package me.potato.webfluxpatterns.sec01.client;

import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec01.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductClient {
    private final WebClient client;

    public ProductClient(@Value("${sec01.product.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<ProductResponse> getProduct(Integer id) {
        return this.client.get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .doOnError(e -> log.error("Error while getting product {}", id, e))
                .onErrorReturn(ProductResponse.of(-1, "No Product", "No Product", 0));
    }
}
