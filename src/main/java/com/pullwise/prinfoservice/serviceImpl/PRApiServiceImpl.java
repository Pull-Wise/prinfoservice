/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pullwise.prinfoservice.serviceImpl;

import com.pullwise.prinfoservice.config.GeminiAPIConfig;
import com.pullwise.prinfoservice.constants.PRInfoServiceConstant;
import com.pullwise.prinfoservice.dto.GitHubWebhookPayload;
import com.pullwise.prinfoservice.response.GithubFileChangeResponse;
import com.pullwise.prinfoservice.response.PrAnalysisResponse;
import com.pullwise.prinfoservice.utils.PRInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;


/**
 *
 * @author Mownesh
 */
@Service
@Slf4j
public class PRApiServiceImpl {

    @Autowired
    GithubAuthServiceImpl githubAuthService;
    @Autowired
    GeminiAPIConfig geminiAPIConfig;
    @Autowired
    PRInfoUtils prInfoUtils;
    @Autowired
    RestTemplate restTemplate;

    public void processGithubWebHook(GitHubWebhookPayload payload){
        this.getChangedFilesOfPR(payload);
    }

    private void getChangedFilesOfPR(GitHubWebhookPayload payload){
        try{

            Integer installationId = this.githubAuthService.getInstallationId(payload);

            String accessToken = this.githubAuthService.getAccessToken(String.valueOf(installationId),String.valueOf(payload.getRepository().getId()));
            //"https://api.github.com/repos/{owner}/{repo}/pulls/{pull_number}/files"
            String url = String.format(
                    PRInfoServiceConstant.GITHUB_API_URL,
                    payload.getRepository().getOwner().getLogin(),
                    payload.getRepository().getName(),
                    payload.getPullRequest().getNumber()
            );

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(accessToken);
            log.info("Access token ->  {}",accessToken);
            log.info("url -> {}",url);
            httpHeaders.set("Accept", "application/vnd.github.v3+json");
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<List<GithubFileChangeResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<GithubFileChangeResponse>>() {}
            );
            postCommentToPR(payload, response, accessToken);

        } catch (Exception e){
            log.error("Exception occured at PRApiServiceImpl :: getChangedFilesOfPR -> {}", e.getMessage());
        }
    }

    private void postCommentToPR(GitHubWebhookPayload payload, ResponseEntity<List<GithubFileChangeResponse>> response, String accessToken) {
        if(response.getStatusCode() == HttpStatus.OK){

        List<PrAnalysisResponse> analysisList = Objects.requireNonNull(response.getBody()).parallelStream().map((file) -> {
            PrAnalysisResponse analysis = new PrAnalysisResponse();
            if(prInfoUtils.isStringNullOrEmpty(file.getPatch()))
                {
                    analysis.setFileName(file.getFilename());
                    analysis.setAnalysisPoints(geminiAPIConfig.getGeminiResponse(file.getPatch()));
                    return analysis;
                }else
                {
                    analysis.setFileName(file.getFilename());
                    analysis.setAnalysisPoints("No Content to analyze !");
                    return analysis;
                }
            }).toList();

        String analysisComment = analysisList.stream()
                .map(analysis -> "File Name : " + "**" + analysis.getFileName() + "**\n" + analysis.getAnalysisPoints()) // Bold filename
                .reduce((analysis1, analysis2) -> analysis1 + "\n\n" + analysis2) // Separate entries with new lines
                .orElse("");

        if(!this.postPRComment(payload, analysisComment, accessToken)){
            log.error("Exception occured at PRApiServiceImpl :: getChangedFilesOfPR -> posting Comments failed -> {}", payload);
        }

        }
    }

    private boolean postPRComment(GitHubWebhookPayload payload, String comment, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        String url = String.format(
                PRInfoServiceConstant.GITHUB_PR_POST_COMMENTS,
                payload.getRepository().getOwner().getLogin(),
                payload.getRepository().getName(),
                payload.getPullRequest().getNumber()
        );

        // Create JSON request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("body", comment);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/vnd.github+json");
        headers.setBearerAuth(accessToken);
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try{
            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        }catch(Exception e){
            log.error("Exception occurred in PRApiServiceImpl :: postPRComment -> {} ", e.getMessage());
            return false;
        }
        return true;
    }

}
