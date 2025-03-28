package com.pullwise.prinfoservice.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrListResponse {

    private String url;
    private Long id;
    private Integer number;
    private String state;
    private User user;
    private String title;
    private String body;
    private List<Label> labels;
    private Milestone milestone;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
    @JsonProperty("closed_at")
    private OffsetDateTime closedAt;
    @JsonProperty("merged_at")
    private OffsetDateTime mergedAt;
    private User assignee;
    private List<User> assignees;
    @JsonProperty("requested_reviewers")
    private List<User> requestedReviewers;
    @JsonProperty("requested_teams")
    private List<Team> requestedTeams;
    private Head head;
    private Base base;
    @JsonProperty("author_association")
    private String authorAssociation;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private String login;
        private Long id;
        @JsonProperty("avatar_url")
        private String avatarUrl;
        private String url;
        @JsonProperty("html_url")
        private String htmlUrl;
        //add other user properties as needed
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Label {
        private Long id;
        private String name;
        private String color;
        private String description;
        //add other label properties as needed
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Milestone {
        private Long id;
        private Integer number;
        private String state;
        private String title;
        private String description;
        @JsonProperty("created_at")
        private OffsetDateTime createdAt;
        @JsonProperty("updated_at")
        private OffsetDateTime updatedAt;
        @JsonProperty("closed_at")
        private OffsetDateTime closedAt;
        @JsonProperty("due_on")
        private OffsetDateTime dueOn;
        //add other milestone properties as needed
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        private Long id;
        private String name;
        private String description;
        @JsonProperty("html_url")
        private String htmlUrl;
        //add other team properties as needed
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Head {
        private String ref;
        private Repo repo;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Base {
        private String ref;
        private User user;
        private Repo repo;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repo{
        private Long id;
        private String name;
        @JsonProperty("full_name")
        private String fullName;
        private User owner;
        private String url;
        @JsonProperty("html_url")
        private String htmlUrl;
    }
}