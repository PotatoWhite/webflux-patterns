package me.potato.webfluxpatterns.sec03.client;

import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec03.dto.ShippingRequest;
import me.potato.webfluxpatterns.sec03.dto.ShippingResponse;
import me.potato.webfluxpatterns.sec03.dto.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ShippingClient {
    private static final String SCHEDULE = "schedule";
    private static final String CANCEL   = "cancel";

    private final WebClient client;

    public ShippingClient(@Value("${sec03.shipping.service}") String url) {
        this.client = WebClient.builder().baseUrl(url).build();
    }

    public Mono<ShippingResponse> schedule(ShippingRequest request) {
        return callShippingService(SCHEDULE, request);
    }

    public Mono<ShippingResponse> cancel(ShippingRequest request) {
        return callShippingService(CANCEL, request);
    }

    private Mono<ShippingResponse> callShippingService(String endPoint, ShippingRequest request) {
        return client.post()
                .uri(endPoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ShippingResponse.class)
                .onErrorReturn(buildErrorResponse(request));
    }

    private ShippingResponse buildErrorResponse(ShippingRequest request) {
        return ShippingResponse.of(request.getOrderId(), request.getQuantity(), Status.FAILURE, null, null);
    }
}
