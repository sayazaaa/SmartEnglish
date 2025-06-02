package com.smartenglishbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicLogData {
    private Integer totalUser;
    private List<ModUseTimeStatistics> totalUseTime;
    private List<ModUseTimeStatistics> averageUseTime;
}
