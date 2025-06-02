package com.smartenglishbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOWordBook {
    private String name;
    private String cover;
    private List<String> content;
}
