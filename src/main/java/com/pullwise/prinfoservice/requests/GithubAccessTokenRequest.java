package com.pullwise.prinfoservice.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubAccessTokenRequest {

    private List<String> repos;

}
