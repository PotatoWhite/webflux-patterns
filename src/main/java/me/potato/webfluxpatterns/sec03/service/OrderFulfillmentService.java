package me.potato.webfluxpatterns.sec03.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import me.potato.webfluxpatterns.sec03.dto.Status;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderFulfillmentService {

    private final List<Orchestrator> orchestrators;

    public Mono<OrchestrationRequestContext> placeOrder(OrchestrationRequestContext ctx) {
        var list = orchestrators.stream()
                .map(orchestrator -> orchestrator.create(ctx))
                .collect(Collectors.toList());

        return Mono.zip(list, a -> a[0])
                .cast(OrchestrationRequestContext.class)
                .doOnNext(this::updateStatus);

    }

    private void updateStatus(OrchestrationRequestContext ctx) {
        var allSuccess = this.orchestrators.stream().allMatch(o -> o.isSuccess().test(ctx));
        var status     = allSuccess ? Status.SUCCESS : Status.FAILURE;
        ctx.setStatus(status);
    }

}
