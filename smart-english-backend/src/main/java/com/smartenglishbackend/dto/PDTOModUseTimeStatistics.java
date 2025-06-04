package com.smartenglishbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDTOModUseTimeStatistics {
    @JsonProperty("function")
    private String modName;
    @JsonProperty("value")
    private Long value;
    public PDTOModUseTimeStatistics(ModUseTimeStatistics modUseTimeStatistics) {
        this.modName = modUseTimeStatistics.getModName();
        this.value = modUseTimeStatistics.getValue();
    }
}
