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
public class RoomReservationResponse {
    private UUID      reservationId;
    private String    city;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String    category;
    private Integer   price;
}
