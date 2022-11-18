package me.potato.webfluxpatterns.sec03.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec03.client.UserClient;
import me.potato.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import me.potato.webfluxpatterns.sec03.dto.Status;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentOrchestrator extends Orchestrator {
    private final UserClient userClient;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext ctx) {
        return userClient.deduct(ctx.getPaymentRequest())
                .doOnNext(ctx::setPaymentResponse)
                .then(Mono.just(ctx));
    }
    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return ctx -> Status.SUCCESS.equals(ctx.getPaymentResponse().getStatus());
    }
    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        return ctx -> Mono.just(ctx)
                .filter(isSuccess()) // 결제가 성공했다면, 환불을 해야한다.
                .map(OrchestrationRequestContext::getPaymentRequest)
                .flatMap(userClient::refund)
                .subscribe();
    }
}
