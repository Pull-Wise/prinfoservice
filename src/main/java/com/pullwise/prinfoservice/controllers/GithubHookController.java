/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pullwise.prinfoservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullwise.prinfoservice.dto.GitHubWebhookPayload;
import com.pullwise.prinfoservice.entity.HookData;
import com.pullwise.prinfoservice.repository.HookDataRepository;
import com.pullwise.prinfoservice.repository.InstallationRepository;
import com.pullwise.prinfoservice.requests.GitHubInstallationRequest;
import com.pullwise.prinfoservice.requests.GithubRepoChangeRequest;
import com.pullwise.prinfoservice.serviceImpl.GithubAppInstallationServiceImpl;
import com.pullwise.prinfoservice.serviceImpl.PRApiServiceImpl;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Mownesh
 */
@Slf4j
@RestController
@RequestMapping("/github-webhook")
public class GithubHookController {
    
    @Autowired
    PRApiServiceImpl prAPIServiceImpl;
    @Autowired
    GithubAppInstallationServiceImpl githubAppInstallationService;
    @Autowired
    HookDataRepository hookDataRepository;

    @PostMapping
    public void handleGitHubWebhook(
            @RequestBody String payload,
            @RequestHeader("X-GitHub-Event") String eventType) {

        log.info("Received GitHub Event: {}", eventType);
        this.persistHookHistoryData(payload,eventType);
        switch (eventType) {
            case "installation":
                GitHubInstallationRequest installationRequest = parsePayload(payload, GitHubInstallationRequest.class);
                log.info("Handling Installation Event");
                log.info(installationRequest.toString());
                githubAppInstallationService.processGithubInstallationHook(installationRequest);
                break;
            case "pull_request":
                GitHubWebhookPayload pullRequestPayload = parsePayload(payload, GitHubWebhookPayload.class);
                prAPIServiceImpl.processGithubWebHook(pullRequestPayload);
                log.info("Handling Pull Request Event");
                break;
            case "installation_repositories":
                GithubRepoChangeRequest githubRepoChangeRequest = parsePayload(payload, GithubRepoChangeRequest.class);
                githubAppInstallationService.processRepoChangeRequest(githubRepoChangeRequest);
                break;
            default:
                log.warn("Unhandled GitHub Event: {}", eventType);
        }
    }

    private <T> T parsePayload(String payload, Class<T> clazz) {
        try {

            return new ObjectMapper().readValue(payload, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GitHub webhook payload", e);
        }
    }

    private void persistHookHistoryData(String payload,String eventType){
        try{
            HookData hookData = HookData.builder()
                    .hookData(payload)
                    .eventType(eventType)
                    .createdBy("PULLWISE")
                    .createdDate(LocalDateTime.now())
                    .lastUpdatedBy("PULLWISE")
                    .lastUpdatedDate(LocalDateTime.now())
                    .build();
            hookDataRepository.save(hookData);
        }catch(Exception e){
            log.error("Error occurred while persistHookHistoryData -> with message {} and payload {}",e.getMessage(),payload);
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
    
}

