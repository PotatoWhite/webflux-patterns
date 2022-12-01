package me.potato.webfluxpatterns.sec05.service;

import me.potato.webfluxpatterns.sec05.dto.ReservationItemRequest;
import me.potato.webfluxpatterns.sec05.dto.ReservationItemResponse;
import me.potato.webfluxpatterns.sec05.dto.ReservationType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public abstract class ReservationHandler {
    protected abstract ReservationType getType();

    protected abstract Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux);
}
