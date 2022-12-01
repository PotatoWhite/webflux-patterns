package me.potato.webfluxpatterns.sec10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ProductAggregate {
    private Integer      id;
    private String       category;
    private String       description;
    private Integer      price;
    private List<Review> reviews;
}
