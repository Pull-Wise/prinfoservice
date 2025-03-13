package com.pullwise.prinfoservice.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubFileChangeResponse {

    private String sha;
    private String filename;
    private String status;
    private int additions;
    private int deletions;
    private int changes;

    @JsonProperty("blob_url")
    private String blobUrl;

    @JsonProperty("raw_url")
    private String rawUrl;

    @JsonProperty("contents_url")
    private String contentsUrl;

    private String patch;
}