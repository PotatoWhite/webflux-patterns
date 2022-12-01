package me.potato.webfluxpatterns.sec05.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ReservationResponse {
    private UUID                          reservationId;
    private Integer                       price;
    private List<ReservationItemResponse> items;
}
