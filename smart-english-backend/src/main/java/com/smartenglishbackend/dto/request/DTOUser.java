package com.smartenglishbackend.dto.request;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("request")
public class DTOUser {
    private String name;
    private String description;
    private String avatar;
    private Integer wordbookid;
}
