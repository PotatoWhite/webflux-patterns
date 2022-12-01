package me.potato.webfluxpatterns.sec04.client;


import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec04.dto.InventoryRequest;
import me.potato.webfluxpatterns.sec04.dto.InventoryResponse;
import me.potato.webfluxpatterns.sec04.dto.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
public class InventoryClient {
    private static final String DEDUCT  = "deduct";
    private static final String RESTORE = "restore";

    private final WebClient client;

    public InventoryClient(@Value("${sec04.inventory.service}") String url) {
        this.client = WebClient.builder().baseUrl(url).build();
    }

    public Mono<InventoryResponse> getInventory(Integer id) {
        return client.get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<InventoryResponse> deduct(InventoryRequest request) {
        return callInventoryService(DEDUCT, request);
    }

    public Mono<InventoryResponse> restore(InventoryRequest request) {
        return callInventoryService(RESTORE, request);
    }

    private Mono<InventoryResponse> callInventoryService(String endPoint, InventoryRequest request) {
        return client.post()
                .uri(endPoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(InventoryResponse.class)
                .onErrorReturn(buildErrorResponse(request));
    }

    private InventoryResponse buildErrorResponse(InventoryRequest request) {
        return InventoryResponse.of(
                null
                , request.getProductId()
                , request.getQuantity()
                , null
                , Status.FAILURE);
    }

}
