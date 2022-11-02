package me.potato.webfluxpatterns.sec01.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@ToString
public class PromotionResponse {
    private Integer   id;
    private String    type;
    private Double    discount;
    private LocalDate endDate;
}
