package me.potato.webfluxpatterns.sec03.service;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec03.client.ProductClient;
import me.potato.webfluxpatterns.sec03.dto.*;
import me.potato.webfluxpatterns.sec03.util.DebugUtil;
import me.potato.webfluxpatterns.sec03.util.OrchestrationUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OrchestratorService {
    private final ProductClient           productClient;
    private final OrderFulfillmentService fulfillmentService;
    private final OrderCancellationService cancellationService;

    public Mono<OrderResponse> placeOrder(Mono<OrderRequest> mono) {
        return mono
                .map(OrchestrationRequestContext::new)
                .flatMap(this::getProduct)
                .doOnNext(OrchestrationUtil::buildRequestContext)
                .flatMap(fulfillmentService::placeOrder)
                .doOnNext(this::doOrderPostProcessing)
                .doOnNext(DebugUtil::print) //just for debugging
                .map(this::toOrderResponse);
    }

    private Mono<OrchestrationRequestContext> getProduct(OrchestrationRequestContext ctx) {
        return productClient.getProduct(ctx.getOrderRequest().getProductId())
                .map(Product::getPrice)
                .doOnNext(ctx::setProductPrice)
                .map(price -> ctx); // when you want to return on success, use map
    }

    private void doOrderPostProcessing(OrchestrationRequestContext ctx) {
        if (Status.FAILURE.equals(ctx.getStatus()))
            this.cancellationService.cancelOrder(ctx);
    }

    private OrderResponse toOrderResponse(OrchestrationRequestContext ctx) {
        var isSuccess    = Status.SUCCESS.equals(ctx.getStatus());
        var address      = isSuccess ? ctx.getShippingResponse().getAddress() : null;
        var deliveryDate = isSuccess ? ctx.getShippingResponse().getExpectedDelivery() : null;

        return OrderResponse.of(
                ctx.getOrderRequest().getUserId()
                , ctx.getOrderRequest().getProductId()
                , ctx.getOrderId()
                , ctx.getStatus()
                , address
                , deliveryDate);
    }

}
