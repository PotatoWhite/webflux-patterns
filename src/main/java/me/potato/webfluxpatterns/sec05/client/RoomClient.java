package me.potato.webfluxpatterns.sec05.client;

import me.potato.webfluxpatterns.sec05.dto.RoomReservationRequest;
import me.potato.webfluxpatterns.sec05.dto.RoomReservationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RoomClient {
    private final WebClient client;

    public RoomClient(@Value("${sec05.room.service}") String url) {
        this.client = WebClient.builder().baseUrl(url).build();
    }

    public Flux<RoomReservationResponse> reserve(Flux<RoomReservationRequest> flux) {
        return this.client.post()
                .body(flux, RoomReservationRequest.class)
                .retrieve()
                .bodyToFlux(RoomReservationResponse.class)
                .onErrorResume(e -> Mono.empty());
    }
}
