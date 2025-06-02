package com.smartenglishbackend.jpaentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SWord {
    private String word;
    private Integer stage;
}
