package com.smartenglishbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOUpdateNWordBook {
    private String word;
    @JsonProperty("nwordbook")
    private Integer nWordBookId;
    private String method;
}
