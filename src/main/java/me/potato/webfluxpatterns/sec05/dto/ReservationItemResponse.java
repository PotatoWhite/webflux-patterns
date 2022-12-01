package me.potato.webfluxpatterns.sec05.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ReservationItemResponse {
    private UUID            reservationId;

    private ReservationType type;
    private String          category;
    private String          city;
    private LocalDate       from;
    private LocalDate       to;

    private Integer price;
}
