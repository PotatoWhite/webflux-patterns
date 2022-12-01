package me.potato.webfluxpatterns.sec06.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@ToString
public class Product {
    private Integer id;
    private String  category;
    private String  description;
    private Integer price;
}
