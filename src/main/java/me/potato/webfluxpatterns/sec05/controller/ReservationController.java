package me.potato.webfluxpatterns.sec05.controller;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec05.dto.ReservationItemRequest;
import me.potato.webfluxpatterns.sec05.dto.ReservationResponse;
import me.potato.webfluxpatterns.sec05.service.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("sec05")
public class ReservationController {
    private final ReservationService service;

    @PostMapping("reserve")
    public Mono<ReservationResponse> reserve(@RequestBody Flux<ReservationItemRequest> flux) {
        return service.reserve(flux);
    }
}
