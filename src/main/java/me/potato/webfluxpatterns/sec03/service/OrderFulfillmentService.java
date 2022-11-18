package me.potato.webfluxpatterns.sec03.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderFulfillmentService {
    private final PaymentOrchestrator paymentOrchestrator;
}
