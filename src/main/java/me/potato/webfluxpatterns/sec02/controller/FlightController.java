package me.potato.webfluxpatterns.sec02.controller;

import lombok.RequiredArgsConstructor;
import me.potato.webfluxpatterns.sec02.dto.FlightResult;
import me.potato.webfluxpatterns.sec02.service.FlightSearchService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("sec02")
public class FlightController {
    private final FlightSearchService flightSearchService;

    @GetMapping(value = "flights/{from}/{to}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<FlightResult> getFlights(@PathVariable String from, @PathVariable String to) {
        return this.flightSearchService.getFlights(from, to);
    }
}
