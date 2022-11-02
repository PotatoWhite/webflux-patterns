package me.potato.webfluxpatterns.sec01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString
public class ProductResponse {
    private Integer id;
    private String  category;
    private String  description;
    private Integer price;
}
