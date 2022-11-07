package me.potato.webfluxpatterns.sec02.service;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec02.client.DeltaClient;
import me.potato.webfluxpatterns.sec02.client.FrontierClient;
import me.potato.webfluxpatterns.sec02.client.JetBlueClient;
import me.potato.webfluxpatterns.sec02.dto.FlightResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class FlightSearchService {
    private final DeltaClient    deltaClient;
    private final FrontierClient frontierClient;
    private final JetBlueClient  jetBlueClient;

    public Flux<FlightResult> getFlights(String from, String to) {
        return Flux.merge(
                        this.deltaClient.getFlights(from, to),
                        this.frontierClient.getFlights(from, to),
                        this.jetBlueClient.getFlight(from, to)
                )
                .take(Duration.ofSeconds(30));
    }
}
