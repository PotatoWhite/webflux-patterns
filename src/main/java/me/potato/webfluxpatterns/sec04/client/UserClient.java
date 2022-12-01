package me.potato.webfluxpatterns.sec04.client;


import lombok.extern.slf4j.Slf4j;
import me.potato.webfluxpatterns.sec04.dto.PaymentRequest;
import me.potato.webfluxpatterns.sec04.dto.PaymentResponse;
import me.potato.webfluxpatterns.sec04.dto.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserClient {
    private static final String DEDUCT = "deduct";
    private static final String REFUND = "refund";

    private final WebClient client;

    public UserClient(@Value("${sec04.user.service}") String url) {
        this.client = WebClient.builder().baseUrl(url).build();
    }

    public Mono<PaymentResponse> getUser(Integer id) {
        return client.get()
                .uri("{id}", id)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<PaymentResponse> deduct(PaymentRequest request) {
        return callUserService(DEDUCT, request);
    }

    public Mono<PaymentResponse> refund(PaymentRequest request) {
        return callUserService(REFUND, request);
    }

    private Mono<PaymentResponse> callUserService(String endPoint, PaymentRequest request) {
        return client.post()
                .uri(endPoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .onErrorReturn(buildErrorResponse(request));
    }

    private PaymentResponse buildErrorResponse(PaymentRequest request) {
        return PaymentResponse.of(
                null
                , request.getUserId()
                , null
                , request.getAmount()
                , Status.FAILURE);
    }

}
