package me.potato.webfluxpatterns.sec04.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class InventoryRequest {
    private UUID    paymentId;
    private Integer productId;
    private Integer quantity;
}