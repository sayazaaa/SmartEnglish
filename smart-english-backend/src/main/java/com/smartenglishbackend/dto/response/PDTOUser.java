package com.smartenglishbackend.dto.response;

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
}
