package me.potato.webfluxpatterns.sec10.controller;

import me.potato.webfluxpatterns.sec10.dto.ProductAggregate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BulkheadTest {
    private WebClient client;

    @BeforeAll
    public void setup() {
        this.client = WebClient.builder()
                .baseUrl("http://localhost:8080/")
                .build();
    }

    @Test
    public void concurrentRequests() {
        var flux = Flux.merge(fibRequests(), productRequest());
        StepVerifier.create(flux)
                .verifyComplete();
    }

    private Mono<Void> fibRequests() {
        return Flux.range(1, 40)
                .flatMap(i -> this.client.get().uri("sec10/fib/46").retrieve().bodyToMono(Long.class))
                .doOnNext(this::print)
                .then();
    }

    private Mono<Void> productRequest() {
        return Mono.delay(Duration.ofMillis(100))
                .thenMany(Flux.range(1, 40))
                .flatMap(i -> this.client.get().uri("sec10/product/1").retrieve().bodyToMono(ProductAggregate.class))
                .map(ProductAggregate::getCategory)
                .doOnNext(this::print)
                .then();
    }

    // Thread Name 과 현재시간을 포함해 출력
    private void print(Object o) {
        System.out.println(Thread.currentThread().getName() + " : " + LocalDateTime.now() + " : " + o);
    }

}