package com.smartenglishbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOUpdateWordBook {
    private Integer id;
    private String method;
    private String word;
}
