package me.potato.webfluxpatterns.sec08.config;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// 안쓸것 같긴한데 이렇게도
@Configuration
public class CircuitBreakerConfig {
    @Bean
    public CircuitBreakerConfigCustomizer reviewService() {
        return CircuitBreakerConfigCustomizer.of("review-service", builder -> builder.minimumNumberOfCalls(4));
    }
}
