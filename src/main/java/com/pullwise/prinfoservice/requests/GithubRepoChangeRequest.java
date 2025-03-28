package com.pullwise.prinfoservice.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pullwise.prinfoservice.dto.Installation;
import com.pullwise.prinfoservice.dto.Repository;
import com.pullwise.prinfoservice.dto.Sender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepoChangeRequest {

    private String action;
    private Installation installation;

    @JsonProperty("repositories_added")
    private List<Repository> repositoriesAdded;

    @JsonProperty("repositories_removed")
    private List<Repository> repositoriesRemoved;

    private Sender sender;

}
