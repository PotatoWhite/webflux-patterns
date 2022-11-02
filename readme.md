# 생각정리

## onErrorResume vs onErrorReturn

```java
public Mono<ProductResponse> getProduct(Integer id) {
    return this.client.get()
            .uri("{id}", id)
            .retrieve()
            .bodyToMono(ProductResponse.class)
            .doOnError(e -> log.error("Error while getting product {}", id, e))
            .onErrorResume(e -> Mono.empty());
}
```


```java
public Mono<ProductResponse> getProduct(Integer id) {
    return this.client.get()
            .uri("{id}", id)
            .retrieve()
            .bodyToMono(ProductResponse.class)
            .doOnError(e -> log.error("Error while getting product {}", id, e))
            .onErrorReturn(ProductResponse.of(-1, "No Product", "No Product", 0));
}
```

### 둘의 차이는 무엇일까?
#### onErrorResume
ProductAggregatorService.aggregate() 사용되는 Mono.zip 내부에서 poductClient.getProduct() 의 결과로 Mono.empty가 반환되면 pipeline이 끊어진다.
일견, 핵심정보인 product 정보가 없으니 ProductAggregatorService.aggregate() 가 끝나는 것이 맞는 것 같지만, 두 가지 문제점이 보인다.
1. product 정보가 핵심정보가 아니라면?
2. ProductClient가 왜 ProductAggregatorService의 pipeline을 끊어야 하는가?

결론적으로 onErrorReturn으로 약속된 무언가를 반환하는 게 유연해 보인다.