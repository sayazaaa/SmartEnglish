package com.smartenglishbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("wordbookid")
    private Integer wordbookId;
}
