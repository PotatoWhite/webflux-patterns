package me.potato.webfluxpatterns.sec03.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec03.client.ShippingClient;
import me.potato.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import me.potato.webfluxpatterns.sec03.dto.Status;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShippingOrchestrator extends Orchestrator {
    private final ShippingClient shippingClient;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext ctx) {
        return shippingClient.schedule(ctx.getShippingRequest())
                .doOnNext(ctx::setShippingResponse)
                .then(Mono.just(ctx));
    }

    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return ctx -> Status.SUCCESS.equals(ctx.getShippingResponse().getStatus());
    }

    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        return ctx -> Mono.just(ctx)
                .filter(isSuccess()) // 결제가 성공했다면, 환불을 해야한다.
                .map(OrchestrationRequestContext::getShippingRequest)
                .flatMap(shippingClient::cancel)
                .subscribe();
    }
}
