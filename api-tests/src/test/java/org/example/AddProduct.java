package org.example;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AddProduct {
    @JsonProperty("product_id")
    private Integer id;
    private Integer quantity;
}
