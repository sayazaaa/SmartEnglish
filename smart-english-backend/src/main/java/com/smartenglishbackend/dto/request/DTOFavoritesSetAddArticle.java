package com.smartenglishbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOFavoritesSetAddArticle {
    @JsonProperty("favorite_set")
    private Integer favoritesSetId;
    private String article;
    private String method;
}
