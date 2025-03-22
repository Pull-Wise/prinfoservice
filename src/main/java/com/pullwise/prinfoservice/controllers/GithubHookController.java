/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pullwise.prinfoservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullwise.prinfoservice.dto.GitHubWebhookPayload;
import com.pullwise.prinfoservice.requests.GitHubInstallationRequest;
import com.pullwise.prinfoservice.serviceImpl.PRApiServiceImpl;

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

    private static final Set<String> PULL_REQUEST_ACTIONS = Set.of("opened", "reopened");
    private static final Set<String> INSTALLATION_ACTIONS = Set.of("created", "deleted");
    
//    @PostMapping
//    public void handleGithubWebhook(@RequestBody GitHubWebhookPayload payload) {
//        if(Objects.nonNull(payload.action) && INSTALLATION_ACTIONS.contains(payload.action)){
//
//        }
//
//        if(Objects.nonNull(payload.action) && PULL_REQUEST_ACTIONS.contains(payload.action)){
//            prAPIServiceImpl.processGithubWebHook(payload);
//        }else{
//            log.error("Github Action is not supported -> {} ",payload);
//        }
//    }

    @PostMapping
    public void handleGitHubWebhook(
            @RequestBody String payload,
            @RequestHeader("X-GitHub-Event") String eventType) {

        log.info("Received GitHub Event: {}", eventType);

        switch (eventType) {
            case "installation":
                GitHubInstallationRequest installationRequest = parsePayload(payload, GitHubInstallationRequest.class);
                log.info("Handling Installation Event");
                log.info(installationRequest.toString());
                // Process the installation event
                break;

            case "pull_request":
                GitHubWebhookPayload pullRequestPayload = parsePayload(payload, GitHubWebhookPayload.class);
                prAPIServiceImpl.processGithubWebHook(pullRequestPayload);
                log.info("Handling Pull Request Event");
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

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
    
}

