package me.potato.webfluxpatterns.sec02.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec02.dto.FlightResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Service
public class FrontierClient {
    private final WebClient client;

    public FrontierClient(@Value("${sec02.frontier.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<FlightResult> getFlights(String from, String to) {
        return this.client.post()
                .bodyValue(FlightRequest.of(from, to))
                .retrieve()
                .bodyToFlux(FlightResult.class)
                .onErrorResume(ex -> Flux.empty());
    }

    @Data
    @ToString
    @AllArgsConstructor(staticName = "of")
    public static class FlightRequest {
        private String from;
        private String to;
    }
}
