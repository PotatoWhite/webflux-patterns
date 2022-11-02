package me.potato.webfluxpatterns.sec01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec01.client.ProductClient;
import me.potato.webfluxpatterns.sec01.client.PromotionClient;
import me.potato.webfluxpatterns.sec01.client.ReviewClient;
import me.potato.webfluxpatterns.sec01.dto.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductAggregatorService {
    private final ProductClient   productClient;
    private final PromotionClient promotionClient;
    private final ReviewClient    reviewClient;


    public Mono<ProductAggregate> aggregate(Integer id) {
        return Mono.zip(
                        this.productClient.getProduct(id),
                        this.promotionClient.getPromotion(id),
                        this.reviewClient.getReviews(id)
                )
                .filter(tuple -> tuple.getT1().getId() != -1)
                .map(tuple -> toDto(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    private ProductAggregate toDto(ProductResponse product, PromotionResponse promotion, List<Review> reviews) {
        var price           = new Price();
        var amountSaved     = product.getPrice() * promotion.getDiscount() / 100.0;
        var discountedPrice = product.getPrice() - amountSaved;

        price.setListPrice(product.getPrice());
        price.setAmountSaved(amountSaved);
        price.setDiscountPrice(discountedPrice);
        price.setDiscount(promotion.getDiscount());
        price.setEndDate(promotion.getEndDate());

        return ProductAggregate.of(product.getId(), product.getCategory(), product.getDescription(), price, reviews);
    }

    @EventListener
    public void handle(Object event) {
        log.info("------------------------------- Product Event: {}", event);
    }

}
