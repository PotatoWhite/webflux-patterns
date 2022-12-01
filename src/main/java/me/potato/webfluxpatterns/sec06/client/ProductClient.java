package me.potato.webfluxpatterns.sec06.client;

import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec06.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class ProductClient {
    private final WebClient client;

    public ProductClient(@Value("${sec06.product.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<Product> getProduct(Integer id) {
        return this.client.get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .timeout(Duration.ofMillis(500))
                .onErrorResume(e -> Mono.empty());
    }
}
