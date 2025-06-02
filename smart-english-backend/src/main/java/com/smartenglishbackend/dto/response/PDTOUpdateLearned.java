package com.smartenglishbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDTOUpdateLearned {
    @JsonProperty("new_word")
    private String newWord;
    private String message;
}
