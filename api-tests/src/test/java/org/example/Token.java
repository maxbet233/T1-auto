package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)

public class Token {

    @JsonProperty("access_token")
    private String accessToken;
}
