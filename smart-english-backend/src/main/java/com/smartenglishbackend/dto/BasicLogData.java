package com.smartenglishbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicLogData {
    @JsonProperty("total_user")
    private Integer totalUser;
    @JsonProperty("total_usetime")
    private List<PDTOModUseTimeStatistics> totalUseTime;
    @JsonProperty("average_usetime")
    private List<PDTOModUseTimeStatistics> averageUseTime;
}
