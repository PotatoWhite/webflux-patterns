package me.potato.webfluxpatterns.sec01.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class Price {
    private Integer   listPrice;
    private Double    discount;
    private Double    discountPrice;
    private Double    amountSaved;
    private LocalDate endDate;
}
