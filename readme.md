# 생각정리

## Sec01 : Mono.zip - Reactive Aggregate Pattern 
- Mono.zip() : 결과를 객체로 본다면 객체의 생성에 필요한 데이터를 다른 Mono들과 결합하여 사용할 수 있다. (OOP 의 composition)
- 객체로 바라보면 생성단계에서 필요한 정보가 모두 존재해야 객체가 생성된다. 이런 경우 아래에서 설명할 onErrorResume을 통해 Mono.zip 자체를 실패 시킬 수 있다.
- 만일 Mono.zip을 Composition 개념이 아닌 aggregation으로 접근한다면 onErrorResume 보다 onErrorReturn을 사용하고 Mono.zip 이후 에러 핸들링 하는 것이 적합해 보인다.


### onErrorResume vs onErrorReturn

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


## Sec02 : Flux.merge - Reactive Scatter-Gather Pattern
- 동알한 규격의 여러 Flux를 하나의 Flux로 합치는 것이다.
- Flux.merge()는 순서를 보장하지 않는다.
- Flux.mergeSequential()은 순서를 보장한다.

```java
public Flux<FlightResult> getFlights(String from, String to) {
    return Flux.merge(
                    this.deltaClient.getFlights(from, to),
                    this.frontierClient.getFlights(from, to),
                    this.jetBlueClient.getFlight(from, to)
            )
            .take(Duration.ofSeconds(3));
}
```
- 최근 3초안에 들어온 결과만을 처리 할 수 있다. take 이후 ThreadPool 자체가 종료 된다. (folkJoinPool의 parallel을 사용한다.)
