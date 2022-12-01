package me.potato.webfluxpatterns.sec05.service;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec05.client.CarClient;
import me.potato.webfluxpatterns.sec05.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class CarReservationHandler extends ReservationHandler {
    private final CarClient client;

    @Override
    protected ReservationType getType() {
        return ReservationType.CAR;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(this::toCarReservationRequest)
                .transform(client::reserve)
                .map(this::toReservationItemResponse);
    }

    private CarReservationRequest toCarReservationRequest(ReservationItemRequest request) {
        return CarReservationRequest.of(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }

    private ReservationItemResponse toReservationItemResponse(CarReservationResponse response) {
        return ReservationItemResponse.of(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getPickup(),
                response.getDrop(),
                response.getPrice()
        );
    }
}
