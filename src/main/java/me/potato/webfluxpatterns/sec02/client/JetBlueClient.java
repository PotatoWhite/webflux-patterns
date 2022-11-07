package me.potato.webfluxpatterns.sec02.client;

import me.potato.webfluxpatterns.sec02.dto.FlightResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class JetBlueClient {
    private static final String    JETBLUE = "JetBlue";
    private final        WebClient client;

    public JetBlueClient(@Value("${sec02.jetblue.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<FlightResult> getFlight(String from, String to) {
        return this.client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("{from}/{to}")
                        .build(from, to))
                .retrieve()
                .bodyToFlux(FlightResult.class)
                .doOnNext(flightResult -> this.normalizeResponse(flightResult, from, to))
                .onErrorResume(ex -> Flux.empty());
    }

    private void normalizeResponse(FlightResult result, String from, String to) {
        result.setFrom(from);
        result.setTo(to);
        result.setAirline(JETBLUE);
    }
}
