package me.potato.webfluxpatterns.sec04.service;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec04.client.ProductClient;
import me.potato.webfluxpatterns.sec04.dto.*;
import me.potato.webfluxpatterns.sec04.util.DebugUtil;
import me.potato.webfluxpatterns.sec04.util.OrchestrationUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OrchestratorService {
    private final ProductClient            productClient;
    private final OrderFulfillmentService  fulfillmentService;
    private final OrderCancellationService cancellationService;

    public Mono<OrderResponse> placeOrder(Mono<OrderRequest> mono) {
        return mono
                .map(OrchestrationRequestContext::new)
                .flatMap(fulfillmentService::placeOrder)
                .doOnNext(this::doOrderPostProcessing) // for fallback
                .doOnNext(DebugUtil::print) //just for debugging
                .map(this::toOrderResponse);
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
