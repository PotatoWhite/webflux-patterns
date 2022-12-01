package me.potato.webfluxpatterns.sec07.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec07.client.ProductClient;
import me.potato.webfluxpatterns.sec07.client.ReviewClient;
import me.potato.webfluxpatterns.sec07.dto.Product;
import me.potato.webfluxpatterns.sec07.dto.ProductAggregate;
import me.potato.webfluxpatterns.sec07.dto.Review;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductAggregatorService {
    private final ProductClient productClient;
    private final ReviewClient  reviewClient;


    public Mono<ProductAggregate> aggregate(Integer id) {
        return Mono.zip(
                        this.productClient.getProduct(id),
                        this.reviewClient.getReviews(id)
                )
                .filter(tuple -> tuple.getT1().getId() != -1)
                .map(tuple -> toDto(tuple.getT1(), tuple.getT2()));
    }

    private ProductAggregate toDto(Product product, List<Review> reviews) {
        return ProductAggregate.of(
                product.getId(),
                product.getCategory(),
                product.getDescription(),
                product.getPrice(),
                reviews);
    }
}
