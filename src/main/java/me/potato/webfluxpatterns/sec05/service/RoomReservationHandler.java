package me.potato.webfluxpatterns.sec05.service;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec05.client.RoomClient;
import me.potato.webfluxpatterns.sec05.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class RoomReservationHandler extends ReservationHandler {
    private final RoomClient client;

    @Override
    protected ReservationType getType() {
        return ReservationType.ROOM;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(this::toRoomReservationRequest)
                .transform(client::reserve)
                .map(this::toReservationItemResponse);
    }

    private RoomReservationRequest toRoomReservationRequest(ReservationItemRequest request) {
        return RoomReservationRequest.of(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }

    private ReservationItemResponse toReservationItemResponse(RoomReservationResponse response) {
        return ReservationItemResponse.of(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getCheckIn(),
                response.getCheckOut(),
                response.getPrice()
        );
    }
}
