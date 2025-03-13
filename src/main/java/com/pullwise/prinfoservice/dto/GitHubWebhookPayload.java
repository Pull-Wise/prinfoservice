/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pullwise.prinfoservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author Mownesh
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubWebhookPayload {

    @JsonProperty("type")
    public String type;
    public String action;

    @JsonProperty("pull_request")
    public PullRequest pullRequest;

    @JsonProperty("repository")
    public Repo repository;

}
