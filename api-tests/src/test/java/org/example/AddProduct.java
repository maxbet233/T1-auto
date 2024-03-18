package org.example;
import com.fasterxml.jackson.annotation.JsonProperty;
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
