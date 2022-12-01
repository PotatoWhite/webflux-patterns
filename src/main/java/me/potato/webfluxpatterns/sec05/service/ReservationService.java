package me.potato.webfluxpatterns.sec05.service;

import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec05.dto.ReservationItemRequest;
import me.potato.webfluxpatterns.sec05.dto.ReservationItemResponse;
import me.potato.webfluxpatterns.sec05.dto.ReservationResponse;
import me.potato.webfluxpatterns.sec05.dto.ReservationType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservationService {
    private final Map<ReservationType, ReservationHandler> map;
    private final ApplicationEventPublisher                publisher;

    public ReservationService(List<ReservationHandler> handlers, ApplicationEventPublisher publisher) {
        this.map       = handlers
                .stream()
                .collect(Collectors.toMap(
                        ReservationHandler::getType
                        , Function.identity()
                ));
        this.publisher = publisher;
    }

    public Mono<ReservationResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux
                .groupBy(ReservationItemRequest::getType)   // split the flux into multiple fluxes
                .flatMap(this::aggregator)
                .collectList()
                .map(this::toReservationResponse);
    }

    private Flux<ReservationItemResponse> aggregator(GroupedFlux<ReservationType, ReservationItemRequest> group) {
        return this.map
                .get(group.key())
                .reserve(group);
    }

    private ReservationResponse toReservationResponse(List<ReservationItemResponse> list) {
        return ReservationResponse.of(
                UUID.randomUUID(),
                list.stream().mapToInt(ReservationItemResponse::getPrice).sum(),
                list);
    }
}
