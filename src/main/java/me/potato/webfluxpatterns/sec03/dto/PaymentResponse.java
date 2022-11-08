package me.potato.webfluxpatterns.sec03.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PaymentResponse {
    private Integer userId;
    private String  name;
    private Integer balance;
    private Status  status;
}
