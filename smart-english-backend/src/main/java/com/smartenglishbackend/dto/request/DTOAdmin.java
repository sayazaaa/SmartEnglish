package com.smartenglishbackend.dto.request;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("request")
public class DTOAdmin {
    private String username;
    private String password;
}
