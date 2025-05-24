package com.smartenglishbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PDTOWordBookBasic {
    private Integer id;
    private String name;
    private String cover;
    private Integer wordcount;
}
