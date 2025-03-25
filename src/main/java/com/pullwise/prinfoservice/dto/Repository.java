package com.pullwise.prinfoservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    private Long id;
    private String name;

    @JsonProperty("full_name")
    private String fullName;
    private boolean isPrivate;
}