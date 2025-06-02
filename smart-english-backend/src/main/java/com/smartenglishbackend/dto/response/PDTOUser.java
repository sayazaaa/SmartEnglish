package com.smartenglishbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class PDTOUser {
    private String name;
    private String description;
    private String avatar;
    private PDTOWordBookBasic wordbook;
    @JsonProperty("new_word_count")
    private Integer newWordCount;
    @JsonProperty("today_review")
    private Integer todayReview;
}
