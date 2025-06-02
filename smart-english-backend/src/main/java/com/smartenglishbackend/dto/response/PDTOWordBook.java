package com.smartenglishbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDTOWordBook {
    private Integer id;
    private String name;
    private String cover;
    @JsonProperty("wordcount")
    private Integer wordCount;
}
