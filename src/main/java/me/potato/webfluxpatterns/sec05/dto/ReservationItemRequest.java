package me.potato.webfluxpatterns.sec05.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ReservationItemRequest {
    private ReservationType type;
    private String          category;
    private String          city;
    private LocalDate       from;
    private LocalDate       to;
}
