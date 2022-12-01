package me.potato.webfluxpatterns.sec03.controller;


import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec03.dto.OrderRequest;
import me.potato.webfluxpatterns.sec03.dto.OrderResponse;
import me.potato.webfluxpatterns.sec03.service.OrchestratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("sec03")
public class OrderController {
    private final OrchestratorService service;

    @PostMapping("order")
    public Mono<ResponseEntity<OrderResponse>> placeOrder(@RequestBody Mono<OrderRequest> mono) {
        return this.service.placeOrder(mono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
