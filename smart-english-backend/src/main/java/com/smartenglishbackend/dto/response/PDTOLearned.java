package com.smartenglishbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDTOLearned {
    private String word;
    @JsonProperty("review_date")
    private LocalDate reviewDate;
}
