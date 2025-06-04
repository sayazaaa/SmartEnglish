package com.smartenglishbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOCalcDataReq {
    private String data;
    private String type;
    private LocalDate start;
}
