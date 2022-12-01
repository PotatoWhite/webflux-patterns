package me.potato.webfluxpatterns.sec09.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("sec09")
public class CalculatorController {

    @GetMapping("calculator/{input}")
    @RateLimiter(name = "calculator-service", fallbackMethod = "fallback")
    public Mono<ResponseEntity<Integer>> doubleInput(@PathVariable Integer input) {
        return Mono.fromSupplier(() -> input * 2)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<String>> fallback(Integer input, Throwable t) {
        return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(t.getMessage()));
    }

}
