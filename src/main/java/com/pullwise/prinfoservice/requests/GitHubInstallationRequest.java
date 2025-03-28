package com.pullwise.prinfoservice.requests;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pullwise.prinfoservice.dto.Installation;
import com.pullwise.prinfoservice.dto.Repository;
import com.pullwise.prinfoservice.dto.Sender;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubInstallationRequest {

    private String action;
    private Installation installation;
    @JsonProperty("repositories")
    private List<Repository> repositories;
    private Sender sender;

}
