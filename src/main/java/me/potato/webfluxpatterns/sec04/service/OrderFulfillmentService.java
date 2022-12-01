package me.potato.webfluxpatterns.sec04.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec04.util.OrchestrationUtil;
import me.potato.webfluxpatterns.sec04.client.ProductClient;
import me.potato.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import me.potato.webfluxpatterns.sec04.dto.Product;
import me.potato.webfluxpatterns.sec04.dto.Status;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderFulfillmentService {

    private final ProductClient productClient;
    private final PaymentOrchestrator paymentOrchestrator;
    private final InventoryOrchestrator inventoryOrchestrator;
    private final ShippingOrchestrator shippingOrchestrator;


    public Mono<OrchestrationRequestContext> placeOrder(OrchestrationRequestContext ctx) {
        return this.getProduct(ctx)
                .doOnNext(OrchestrationUtil::buildPaymentRequest)
                .flatMap(this.paymentOrchestrator::create)
                .doOnNext(OrchestrationUtil::buildInventoryRequest)
                .flatMap(this.inventoryOrchestrator::create)
                .doOnNext(OrchestrationUtil::buildShippingRequest)
                .flatMap(this.shippingOrchestrator::create)
                .doOnNext(c -> c.setStatus(Status.SUCCESS))
                .doOnError(e -> ctx.setStatus(Status.FAILURE))
                .onErrorReturn(ctx);
    }

    private Mono<OrchestrationRequestContext> getProduct(OrchestrationRequestContext ctx) {
        return productClient.getProduct(ctx.getOrderRequest().getProductId())
                .map(Product::getPrice)
                .doOnNext(ctx::setProductPrice)
                .map(price -> ctx); // when you want to return on success, use map
    }
}
