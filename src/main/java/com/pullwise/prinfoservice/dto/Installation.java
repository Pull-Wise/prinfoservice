package com.pullwise.prinfoservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pullwise.prinfoservice.requests.GitHubInstallationRequest;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Installation {
    private Long id;
    private Account account;
    @JsonProperty("app_id")
    private Long appId;
    @JsonProperty("app_slug")
    private String appSlug;
}
