package org.example;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AddProduct {
    private String name;
    private String category;
    private Float price;
    private Float discount;
}
