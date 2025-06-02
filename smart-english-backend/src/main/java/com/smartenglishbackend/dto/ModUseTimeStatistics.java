package com.smartenglishbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModUseTimeStatistics{
    @JsonProperty("function")
    private String modName;
    @JsonProperty("duration")
    private Long value;
    ModUseTimeStatistics(String modName, Double value){
        this.modName = modName;
        this.value = value.longValue();
    }
}
