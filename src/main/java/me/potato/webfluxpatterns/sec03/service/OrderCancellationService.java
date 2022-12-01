package me.potato.webfluxpatterns.sec03.service;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderCancellationService {
    private       Sinks.Many<OrchestrationRequestContext> sink;
    private       Flux<OrchestrationRequestContext>       flux;
    private final List<Orchestrator>                      orchestrators;

    @PostConstruct
    public void init() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.flux = this.sink.asFlux().publishOn(Schedulers.boundedElastic());
        orchestrators.forEach(o -> this.flux.subscribe(o.cancel()));
    }

    public void cancelOrder(OrchestrationRequestContext ctx) {
        this.sink.tryEmitNext(ctx);
    }
}
